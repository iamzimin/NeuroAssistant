package com.evg.database.di

import android.content.Context
import androidx.room.Room
import com.evg.database.data.repository.ChatHistoryRepositoryImpl
import com.evg.database.data.storage.ChatDao
import com.evg.database.data.storage.ChatMessageDao
import com.evg.database.data.storage.NeuroAssistantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.evg.database.data.repository.DatabaseRepositoryImpl
import com.evg.database.domain.repository.ChatHistoryRepository
import com.evg.database.domain.repository.DatabaseRepository

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): NeuroAssistantDatabase {
        return Room.databaseBuilder(
            context,
            NeuroAssistantDatabase::class.java,
            NeuroAssistantDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideChatDao(
        database: NeuroAssistantDatabase,
    ): ChatDao {
        return database.chatDao()
    }

    @Provides
    @Singleton
    fun provideChatMessageDao(
        database: NeuroAssistantDatabase,
    ): ChatMessageDao {
        return database.chatMessageDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        chatDao: ChatDao,
        chatMessageDao: ChatMessageDao,
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(
            chatDao = chatDao,
            chatMessageDao = chatMessageDao,
        )
    }

    @Provides
    @Singleton
    fun provideChatHistoryRepository(
        chatMessageDao: ChatMessageDao,
    ): ChatHistoryRepository {
        return ChatHistoryRepositoryImpl(chatMessageDao)
    }
}
