package com.ccs.thaparbitesshop.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<String> {

        return try {

            val result =
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()

            Result.success(
                result.user!!.uid
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(
        idToken: String
    ): Result<Unit> {

        return try {

            val credential =
                GoogleAuthProvider.getCredential(
                    idToken,
                    null
                )

            auth.signInWithCredential(
                credential
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun logout() {
        auth.signOut()
    }
}