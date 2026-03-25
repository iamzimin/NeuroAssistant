package com.evg.login.presentation

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal fun createGoogleSignInRequest(context: Context): GetCredentialRequest? {
    val serverClientId = context.resolveDefaultWebClientId() ?: return null
    val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        serverClientId = serverClientId,
    )
        .build()

    return GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()
}

internal fun Credential.extractGoogleIdTokenOrNull(): String? {
    if (this !is CustomCredential) return null
    if (type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) return null

    return try {
        GoogleIdTokenCredential.createFrom(data).idToken
    } catch (e: GoogleIdTokenParsingException) {
        FirebaseCrashlytics.getInstance().recordException(e)
        null
    }
}

private fun Context.resolveDefaultWebClientId(): String? {
    val resourceId = resources.getIdentifier("default_web_client_id", "string", packageName)
    if (resourceId == 0) return null

    return getString(resourceId).takeIf { it.isNotBlank() }
}