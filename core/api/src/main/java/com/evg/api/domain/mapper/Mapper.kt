package com.evg.api.domain.mapper

import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.GigaChatError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

fun Throwable.toFirebaseError(): FirebaseError {
    FirebaseCrashlytics.getInstance().recordException(this)
    return when (this) {
        is FirebaseAuthWeakPasswordException -> FirebaseError.WEAK_PASSWORD
        is FirebaseAuthUserCollisionException -> FirebaseError.EMAIL_ALREADY_IN_USE
        is FirebaseTooManyRequestsException -> FirebaseError.TOO_MANY_REQUESTS
        is FirebaseNetworkException -> FirebaseError.NETWORK_ERROR
        is FirebaseAuthInvalidCredentialsException -> FirebaseError.INVALID_EMAIL
        is FirebaseAuthException -> {
            when (this.errorCode) {
                "ERROR_INVALID_CREDENTIAL" -> FirebaseError.INVALID_CREDENTIAL
                "ERROR_USER_NOT_FOUND" -> FirebaseError.USER_NOT_FOUND
                "ERROR_USER_DISABLED" -> FirebaseError.USER_DISABLED
                else -> FirebaseError.UNKNOWN
            }
        }
        else -> FirebaseError.UNKNOWN
    }
}

fun Throwable.toGigaChatError(): GigaChatError {
    FirebaseCrashlytics.getInstance().recordException(this)
    return when (this) {
        is HttpException -> when (code()) {
            400 -> GigaChatError.BAD_REQUEST
            401 -> GigaChatError.UNAUTHORIZED
            403 -> GigaChatError.FORBIDDEN
            404 -> GigaChatError.NOT_FOUND
            408 -> GigaChatError.REQUEST_TIMEOUT
            429 -> GigaChatError.TOO_MANY_REQUESTS
            in 500..599 -> GigaChatError.SERVER_ERROR
            else -> GigaChatError.UNKNOWN
        }

        is SocketTimeoutException -> GigaChatError.REQUEST_TIMEOUT
        is SerializationException -> GigaChatError.SERIALIZATION
        is UnknownHostException -> GigaChatError.UNKNOWN_HOST
        is ProtocolException -> GigaChatError.PROTOCOL_EXCEPTION
        is ConnectException -> GigaChatError.CONNECT_EXCEPTION
        is SSLHandshakeException -> GigaChatError.SSL_HANDSHAKE
        else -> GigaChatError.UNKNOWN
    }
}