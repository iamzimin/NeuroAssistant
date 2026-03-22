package com.evg.database.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evg.database.domain.model.ChatDBO

@Database(
    entities = [ChatDBO::class],
    version = 1,
)
abstract class NeuroAssistantDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        const val DATABASE_NAME = "neuro_assistant.db"
    }
}
