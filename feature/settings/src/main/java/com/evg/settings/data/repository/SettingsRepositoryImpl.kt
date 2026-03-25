package com.evg.settings.data.repository

import android.util.Base64
import com.evg.api.data.remote.GigaChatCredentials
import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.repository.GigaChatApiRepository
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import com.evg.database.domain.repository.DatabaseRepository
import com.evg.settings.domain.mapper.toCompressedBase64
import com.evg.settings.domain.mapper.toProfileInfo
import com.evg.settings.domain.model.ProfileInfo
import com.evg.settings.domain.repository.SettingsRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

/**
 * Репозиторий для управления настройками пользователя, профилем и балансом токенов GigaChat
 *
 * @property firebaseApiRepository Репозиторий для работы с Firebase
 * @property gigaChatApiRepository Репозиторий для работы с GigaChat API
 * @property databaseRepository Репозиторий для работы с локальной базой данных
 */
class SettingsRepositoryImpl @Inject constructor(
    private val firebaseApiRepository: FirebaseApiRepository,
    private val gigaChatApiRepository: GigaChatApiRepository,
    private val databaseRepository: DatabaseRepository,
) : SettingsRepository {
    /**
     * Получение информации профиля текущего пользователя
     *
     * @return [ProfileInfo] с данными пользователя или null, если пользователь не авторизован
     */
    override suspend fun getProfileInfo(): ProfileInfo? {
        val currentUser = firebaseApiRepository.getCurrentUser() ?: return null
        val photoBytes = when (val result = firebaseApiRepository.getProfilePhotoBase64()) {
            is ServerResult.Success -> {
                result.data?.let {
                    runCatching {
                        Base64.decode(it, Base64.DEFAULT)
                    }.getOrNull()
                }
            }
            is ServerResult.Error -> null
        }

        return currentUser.toProfileInfo(photoBytes)
    }

    /**
     * Получение баланса токенов пользователя в GigaChat
     *
     * @return [ServerResult] с числом токенов или [GigaChatError] при ошибке
     */
    override suspend fun getGigaChatTokenBalance(): ServerResult<Int, GigaChatError> {
        return gigaChatApiRepository.getTokenBalance(GigaChatCredentials.MODEL)
    }

    /**
     * Обновление профиля пользователя: имя и фотография
     *
     * @param displayName Новое отображаемое имя пользователя
     * @param photoBytes Фотография пользователя в виде [ByteArray]
     * @param photoFileExtension Расширение файла фотографии
     * @return [ServerResult] с обновленным [ProfileInfo] или [FirebaseError] при ошибке
     */
    override suspend fun updateProfile(
        displayName: String?,
        photoBytes: ByteArray?,
        photoFileExtension: String?,
    ): ServerResult<ProfileInfo, FirebaseError> {
        val photoBase64 = photoBytes?.toCompressedBase64(
            maxSide = 512,
            quality = 80,
        )

        return when (
            val result = firebaseApiRepository.updateProfile(
                displayName = displayName,
                photoBase64 = photoBase64,
                photoFileExtension = photoFileExtension,
            )
        ) {
            is ServerResult.Success -> {
                val profileInfo = getProfileInfo()

                if (profileInfo == null) {
                    FirebaseCrashlytics.getInstance().recordException(Exception("getProfileInfo is null in profileInfo function SettingsRepositoryImpl"))
                    ServerResult.Error(FirebaseError.UNKNOWN)
                } else {
                    ServerResult.Success(profileInfo)
                }
            }

            is ServerResult.Error -> ServerResult.Error(result.error)
        }
    }

    /**
     * Выход пользователя из приложения и очистка локальных данных
     */
    override suspend fun logout() {
        try {
            databaseRepository.clearAll()
        } finally {
            firebaseApiRepository.signOut()
        }
    }
}