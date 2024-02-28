package dev.jorgealejandro.tm.discoveryapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.RemoteKeysEntity
import dev.jorgealejandro.tm.discoveryapi.data.local.converters.EventImagesConverter
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.EventDao
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.RemoteKeyDao

@Database(
    entities = [
        EventDataEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        EventImagesConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}