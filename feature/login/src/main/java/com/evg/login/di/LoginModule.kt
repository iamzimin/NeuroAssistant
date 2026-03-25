package com.evg.login.di

import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.login.data.repository.LoginRepositoryImpl
import com.evg.login.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl,
    ): LoginRepository {
        return loginRepositoryImpl
    }
}
