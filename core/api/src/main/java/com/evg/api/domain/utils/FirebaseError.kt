package com.evg.api.domain.utils

enum class FirebaseError : Error {
    INVALID_CREDENTIAL,
    USER_NOT_FOUND,
    USER_DISABLED,
    EMAIL_ALREADY_IN_USE,
    WEAK_PASSWORD,
    NETWORK_ERROR,
    TOO_MANY_REQUESTS,
    UNKNOWN
}