package com.evg.chat.di

import com.evg.chat.data.repository.ChatRepositoryImpl
import com.evg.chat.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl,
    ): ChatRepository {
        return chatRepositoryImpl
    }
}