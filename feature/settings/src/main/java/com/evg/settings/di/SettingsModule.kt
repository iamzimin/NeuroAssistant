package com.evg.settings.di

import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.repository.GigaChatApiRepository
import com.evg.database.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.settings.data.repository.SettingsRepositoryImpl
import com.evg.settings.domain.repository.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        firebaseApiRepository: FirebaseApiRepository,
        gigaChatApiRepository: GigaChatApiRepository,
        databaseRepository: DatabaseRepository,
    ): SettingsRepository {
        return SettingsRepositoryImpl(
            firebaseApiRepository = firebaseApiRepository,
            gigaChatApiRepository = gigaChatApiRepository,
            databaseRepository = databaseRepository,
        )
    }
}