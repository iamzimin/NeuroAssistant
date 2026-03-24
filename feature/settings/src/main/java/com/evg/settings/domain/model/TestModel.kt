package com.evg.settings.domain.model

data class ProfileInfo(
    val displayName: String,
    val email: String,
    val phoneNumber: String?,
    val photoBytes: ByteArray?,
    val photoUrl: String?,
)