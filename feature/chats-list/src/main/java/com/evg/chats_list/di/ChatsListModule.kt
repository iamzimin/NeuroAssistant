package com.evg.chats_list.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.chats_list.data.repository.ChatsListRepositoryImpl
import com.evg.chats_list.domain.repository.ChatsListRepository
import com.evg.database.domain.repository.DatabaseRepository

@Module
@InstallIn(SingletonComponent::class)
object ChatsListModule {

    @Provides
    @Singleton
    fun provideChatsListRepository(
        databaseRepository: DatabaseRepository,
    ): ChatsListRepository {
        return ChatsListRepositoryImpl(databaseRepository)
    }
}