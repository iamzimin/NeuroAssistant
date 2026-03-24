package com.evg.settings.domain.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.scale
import com.evg.settings.domain.model.ProfileInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

fun FirebaseUser.toProfileInfo(photoBytes: ByteArray?): ProfileInfo {
    val isPhoneAccount = providerData.any { provider ->
        provider.providerId == PhoneAuthProvider.PROVIDER_ID
    }

    return ProfileInfo(
        displayName = displayName.orEmpty(),
        email = email.orEmpty(),
        phoneNumber = phoneNumber?.takeIf { isPhoneAccount },
        photoBytes = photoBytes,
        photoUrl = photoUrl?.toString(),
    )
}

fun ByteArray.toCompressedBase64(
    maxSide: Int = 512,
    quality: Int = 50,
): String? {
    if (isEmpty()) return null

    val decodedBitmap = BitmapFactory.decodeByteArray(this, 0, size) ?: return null

    val scaledBitmap = run {
        val currentMaxSide = maxOf(decodedBitmap.width, decodedBitmap.height)

        if (currentMaxSide <= maxSide) {
            decodedBitmap
        } else {
            val ratio = maxSide.toFloat() / currentMaxSide.toFloat()
            val newWidth = (decodedBitmap.width * ratio).roundToInt().coerceAtLeast(1)
            val newHeight = (decodedBitmap.height * ratio).roundToInt().coerceAtLeast(1)

            decodedBitmap.scale(newWidth, newHeight)
        }
    }

    val outputStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

    if (scaledBitmap != decodedBitmap) {
        decodedBitmap.recycle()
    }

    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}