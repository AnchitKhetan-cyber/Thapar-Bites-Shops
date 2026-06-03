package com.ccs.thaparbitesshop.data.repository


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * AuthRepository
 *
 * Single source of truth for all Firebase Auth operations.
 * The ViewModel talks to this — never to FirebaseAuth directly.
 *
 * Sealed result types keep error handling explicit and exhaustive.
 */
class AuthRepository(
    private val auth: FirebaseAuth = Firebase.auth
) {

    // ── Current user ──────────────────────────────────────────────────────────

    /** Null if nobody is signed in. */
    val currentUser: FirebaseUser? get() = auth.currentUser

    /** True if a Firebase session is active (persisted across restarts). */
    val isLoggedIn: Boolean get() = auth.currentUser != null

    /**
     * Emits the current [FirebaseUser] (or null) whenever auth state changes.
     * Collect this in a ViewModel to react to sign-in / sign-out / token refresh.
     */
    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    // ── Sign in ───────────────────────────────────────────────────────────────

    /**
     * Authenticates a shop owner by email + password.
     * Returns [AuthResult.Success] with the signed-in user, or
     * [AuthResult.Error] with a human-readable message.
     */
    suspend fun signIn(email: String, password: String): AuthResult<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult.Error("Authentication failed.")
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.toReadableMessage())
        }
    }

    // ── Password reset ────────────────────────────────────────────────────────

    /**
     * Sends a password-reset email.
     * Returns [AuthResult.Success] (Unit) or [AuthResult.Error].
     */
    suspend fun sendPasswordReset(email: String): AuthResult<Unit> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.toReadableMessage())
        }
    }

    // ── Sign out ──────────────────────────────────────────────────────────────

    fun signOut() = auth.signOut()
}

// ── Sealed result ─────────────────────────────────────────────────────────────

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

// ── Extension: Firebase exception → readable string ───────────────────────────

private fun Exception.toReadableMessage(): String {
    val msg = message ?: return "Something went wrong. Try again."
    return when {
        msg.contains("no user record")               -> "No account found with this email."
        msg.contains("password is invalid") ||
                msg.contains("wrong-password")               -> "Incorrect password. Please try again."
        msg.contains("badly formatted")              -> "Invalid email address."
        msg.contains("too-many-requests")            -> "Too many attempts. Try again later."
        msg.contains("network")                      -> "No internet connection."
        msg.contains("user-disabled")                -> "This account has been disabled."
        msg.contains("email-already-in-use")         -> "This email is already registered."
        else                                         -> "Login failed: $msg"
    }
}