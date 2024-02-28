package dev.jorgealejandro.tm.discoveryapi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.RemoteKeysEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeysEntity>)

    @Query("SELECT * FROM ${EntityConstants.TABLE_NAME_REMOTE_KEYS} WHERE eventId = :id")
    suspend fun remoteKeysEventId(id: String): RemoteKeysEntity?

    @Query("DELETE FROM ${EntityConstants.TABLE_NAME_REMOTE_KEYS}")
    suspend fun deleteAll()
}