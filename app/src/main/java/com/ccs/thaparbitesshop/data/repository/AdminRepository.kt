package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.data.model.FirestorePaths
import com.ccs.thaparbitesshop.data.model.ShopOwner
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * AdminRepository
 *
 * Handles admin-only operations:
 *  1. Verify the currently signed-in user is an admin (check admins/{uid} in Firestore)
 *  2. Create a new shop owner account:
 *       a. Create Firebase Auth user (email + password)
 *       b. Write ShopOwner document to shops/{newUid}
 *       c. Re-sign-in the admin (creating a new Auth user signs out the current session)
 *  3. List all shop owners
 *  4. Toggle shop active status
 *  5. Delete a shop owner
 *
 * ⚠️  IMPORTANT — re-sign-in after createUser:
 *  Firebase Client SDK's createUserWithEmailAndPassword() automatically
 *  signs in the newly created user, logging out the admin.
 *  We save the admin credentials in memory and re-authenticate after creation.
 *  For a production app, move account creation to a Firebase Cloud Function
 *  so the admin session is never interrupted.
 */
class AdminRepository(
    private val auth: FirebaseFirestore = Firebase.firestore,
    private val fbAuth: FirebaseAuth    = Firebase.auth
) {

    // ── Admin check ───────────────────────────────────────────────────────────

    /**
     * Returns true if the current user has a document in the admins collection.
     * Call this after login to guard the admin dashboard route.
     */
    suspend fun isCurrentUserAdmin(): Boolean {
        val uid = fbAuth.currentUser?.uid ?: return false
        return try {
            val doc = auth.collection(FirestorePaths.ADMINS).document(uid).get().await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    // ── Create shop owner ─────────────────────────────────────────────────────

    /**
     * Creates a Firebase Auth account + Firestore document for a new shop owner.
     *
     * @param shopOwner  Filled ShopOwner (uid will be set automatically)
     * @param password   Initial password for the new account
     * @param adminEmail Admin's own email — used to re-sign-in after creation
     * @param adminPass  Admin's own password — used to re-sign-in after creation
     */
    suspend fun createShopOwner(
        shopOwner: ShopOwner,
        password: String,
        adminEmail: String,
        adminPass: String
    ): AuthResult<ShopOwner> {
        return try {
            val adminUid = fbAuth.currentUser?.uid
                ?: return AuthResult.Error("Not signed in as admin.")

            // 1. Create Firebase Auth account — this signs out the admin
            val result = fbAuth.createUserWithEmailAndPassword(
                shopOwner.email.trim(), password
            ).await()

            val newUid = result.user?.uid
                ?: return AuthResult.Error("Failed to get new user ID.")

            // 2. Write Firestore document
            val docData = shopOwner.copy(
                uid            = newUid,
                createdAt      = Timestamp.now(),
                createdByAdmin = adminUid
            )
            auth.collection(FirestorePaths.SHOPS)
                .document(newUid)
                .set(docData.toMap())
                .await()

            // 3. Re-sign-in the admin (createUser logs them out)
            fbAuth.signInWithEmailAndPassword(adminEmail, adminPass).await()

            AuthResult.Success(docData)

        } catch (e: Exception) {
            // Attempt to re-sign-in admin even on failure
            try { fbAuth.signInWithEmailAndPassword(adminEmail, adminPass).await() }
            catch (_: Exception) {}

            AuthResult.Error(e.toReadableMessage())
        }
    }

    // ── List all shops ────────────────────────────────────────────────────────

    suspend fun getAllShops(): AuthResult<List<ShopOwner>> {
        return try {
            val snapshot = auth.collection(FirestorePaths.SHOPS).get().await()
            val shops = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ShopOwner::class.java)
            }
            AuthResult.Success(shops)
        } catch (e: Exception) {
            AuthResult.Error(e.toReadableMessage())
        }
    }

    // ── Toggle active ─────────────────────────────────────────────────────────

    suspend fun setShopActive(shopUid: String, isActive: Boolean): AuthResult<Unit> {
        return try {
            auth.collection(FirestorePaths.SHOPS)
                .document(shopUid)
                .update("isActive", isActive)
                .await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.toReadableMessage())
        }
    }

    // ── Delete shop ───────────────────────────────────────────────────────────
    // Note: This only removes the Firestore doc.
    // To also delete the Auth account, use a Cloud Function.

    suspend fun deleteShop(shopUid: String): AuthResult<Unit> {
        return try {
            auth.collection(FirestorePaths.SHOPS)
                .document(shopUid)
                .delete()
                .await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.toReadableMessage())
        }
    }
}

// ── Exception mapper (reused from AuthRepository) ─────────────────────────────

private fun Exception.toReadableMessage(): String {
    val msg = message ?: return "Something went wrong."
    return when {
        msg.contains("email-already-in-use") -> "This email is already registered."
        msg.contains("weak-password")        -> "Password must be at least 6 characters."
        msg.contains("badly formatted")      -> "Invalid email address."
        msg.contains("network")              -> "No internet connection."
        msg.contains("permission-denied")    -> "Admin access required."
        else                                 -> msg
    }
}