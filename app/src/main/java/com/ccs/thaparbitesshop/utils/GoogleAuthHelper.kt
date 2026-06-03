package com.ccs.thaparbitesshop.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthHelper(
    private val context: Context
) {

    suspend fun signIn(): String? {

        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(
                    googleIdOption
                )
                .build()

        val credentialManager =
            CredentialManager.create(context)

        val result =
            credentialManager.getCredential(
                context = context,
                request = request
            )

        val googleCredential =
            GoogleIdTokenCredential.createFrom(
                result.credential.data
            )

        return googleCredential.idToken
    }

    companion object {

        private const val WEB_CLIENT_ID =
            "233876426652-r8ua1rehpvqpe6bgserh4u7qjfgv3aps.apps.googleusercontent.com"
    }
}