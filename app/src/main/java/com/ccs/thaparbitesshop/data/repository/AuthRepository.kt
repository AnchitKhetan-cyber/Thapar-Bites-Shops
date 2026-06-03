package com.ccs.thaparbitesshop.data.repository

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun register(
        email: String,
        password: String
    ): Result<String>

    suspend fun signInWithGoogle(
        idToken: String
    ): Result<Unit>

    fun currentUserId(): String?

    fun logout()
}