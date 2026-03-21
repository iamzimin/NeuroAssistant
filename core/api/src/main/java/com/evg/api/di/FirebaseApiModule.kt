package com.evg.api.di

import com.evg.api.data.repository.FirebaseRepositoryImpl
import com.evg.api.domain.repository.FirebaseApiRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseApiModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseApiRepository(
        firebaseAuth: FirebaseAuth
    ): FirebaseApiRepository {
        return FirebaseRepositoryImpl(
            firebaseAuth = firebaseAuth,
        )
    }
}