package com.evg.chat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.chat.data.repository.ChatRepositoryImpl
import com.evg.chat.domain.repository.ChatRepository

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        @ApplicationContext context: Context,
    ): ChatRepository {
        return ChatRepositoryImpl()
    }
}