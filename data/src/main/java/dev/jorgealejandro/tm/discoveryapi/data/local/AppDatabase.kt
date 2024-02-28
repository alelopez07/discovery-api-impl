package dev.jorgealejandro.tm.discoveryapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.EventDao
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.RemoteKeyDao
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.EventEntity
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.RemoteKeysEntity

@Database(
    entities = [
        EventEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}