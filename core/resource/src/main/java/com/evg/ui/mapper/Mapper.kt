package com.evg.ui.mapper

import android.content.Context
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.GigaChatError
import com.evg.resource.R

fun GigaChatError.toErrorMessage(context: Context): String {
    return when (this) {
        GigaChatError.BAD_REQUEST -> context.getString(R.string.bad_request)
        GigaChatError.UNAUTHORIZED -> context.getString(R.string.error_unauthorized)
        GigaChatError.REQUEST_TIMEOUT -> context.getString(R.string.error_request_timeout)
        GigaChatError.TOO_MANY_REQUESTS -> context.getString(R.string.error_too_many_requests)
        GigaChatError.FORBIDDEN -> context.getString(R.string.error_forbidden)
        GigaChatError.NOT_FOUND -> context.getString(R.string.error_not_found)
        GigaChatError.SERVER_ERROR -> context.getString(R.string.error_server)
        GigaChatError.SERIALIZATION -> context.getString(R.string.error_serialization)
        GigaChatError.UNKNOWN_HOST -> context.getString(R.string.error_unknown_host)
        GigaChatError.PROTOCOL_EXCEPTION -> context.getString(R.string.error_protocol)
        GigaChatError.CONNECT_EXCEPTION -> context.getString(R.string.error_connection)
        GigaChatError.SSL_HANDSHAKE -> context.getString(R.string.ssl_handshake)
        GigaChatError.UNKNOWN -> context.getString(R.string.error_unknown)
    }
}

fun FirebaseError.toErrorMessage(context: Context): String {
    return when (this) {
        FirebaseError.INVALID_CREDENTIAL -> context.getString(R.string.error_invalid_credentials)
        FirebaseError.USER_NOT_FOUND -> context.getString(R.string.error_user_not_found)
        FirebaseError.USER_DISABLED -> context.getString(R.string.error_user_disabled)
        FirebaseError.EMAIL_ALREADY_IN_USE -> context.getString(R.string.error_email_already_in_use)
        FirebaseError.WEAK_PASSWORD -> context.getString(R.string.error_weak_password)
        FirebaseError.NETWORK_ERROR -> context.getString(R.string.error_network)
        FirebaseError.TOO_MANY_REQUESTS -> context.getString(R.string.error_too_many_requests)
        FirebaseError.UNKNOWN -> context.getString(R.string.error_unknown)
    }
}