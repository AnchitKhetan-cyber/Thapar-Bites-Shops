package com.ccs.thaparbitesshop.data.repository

import com.ccs.thaparbitesshop.data.model.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MenuRepository {

    // ── Real-time stream ───────────────────────────────────────────────────────

    override fun getMenuStream(shopId: String): Flow<List<MenuItem>> =
        callbackFlow {

            val listener = firestore
                .collection("shops")
                .document(shopId)
                .collection("menuItems")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) return@addSnapshotListener

                    val items = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(MenuItem::class.java)?.copy(id = doc.id)
                    } ?: emptyList()

                    trySend(items)
                }

            awaitClose { listener.remove() }
        }

    // ── Write operations ───────────────────────────────────────────────────────

    override suspend fun addItem(shopId: String, item: MenuItem): Result<String> {
        return try {
            val ref = firestore
                .collection("shops")
                .document(shopId)
                .collection("menuItems")
                .add(item.copy(id = ""))   // Firestore will assign id
                .await()

            Result.success(ref.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleAvailability(
        shopId: String,
        itemId: String,
        isAvailable: Boolean
    ): Result<Unit> {
        return try {
            firestore
                .collection("shops")
                .document(shopId)
                .collection("menuItems")
                .document(itemId)
                .update("isAvailable", isAvailable)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteItem(shopId: String, itemId: String): Result<Unit> {
        return try {
            firestore
                .collection("shops")
                .document(shopId)
                .collection("menuItems")
                .document(itemId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}