package com.evg.chats_list.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.chats_list.data.repository.ChatsListRepositoryImpl
import com.evg.chats_list.domain.repository.ChatsListRepository

@Module
@InstallIn(SingletonComponent::class)
object ChatsListModule {

    @Provides
    @Singleton
    fun provideChatsListRepository(
        @ApplicationContext context: Context,
    ): ChatsListRepository {
        return ChatsListRepositoryImpl()
    }
}