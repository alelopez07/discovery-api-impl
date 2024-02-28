package dev.jorgealejandro.tm.discoveryapi.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEvents(events: List<EventEntity>)

    @Query(
        "SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT} WHERE " +
                "name LIKE :queryString ORDER BY name ASC"
    )
    fun eventsName(queryString: String): PagingSource<Int, EventEntity>

    @Query("SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT} LIMIT :size OFFSET :offset")
    suspend fun getAll(size: Int, offset: Int): List<EventEntity>

    @Query("SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT}")
    fun getPagingSource(): PagingSource<Int, EventEntity>

    @Query("DELETE FROM ${EntityConstants.TABLE_NAME_EVENT}")
    suspend fun clearEvents()
}