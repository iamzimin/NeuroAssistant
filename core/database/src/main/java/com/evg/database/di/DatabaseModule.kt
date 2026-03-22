package com.evg.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.database.data.repository.DatabaseRepositoryImpl
import com.evg.database.domain.repository.DatabaseRepository

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        @ApplicationContext context: Context,
    ): DatabaseRepository {
        return DatabaseRepositoryImpl()
    }
}