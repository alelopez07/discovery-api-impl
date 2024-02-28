package dev.jorgealejandro.tm.discoveryapi.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEvents(events: List<EventDataEntity>)

    @Query(
        "SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT} WHERE " +
                "name LIKE :queryString ORDER BY name ASC"
    )
    fun eventsByName(queryString: String): PagingSource<Int, EventDataEntity>

    @Query("SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT} LIMIT :size OFFSET :offset")
    suspend fun getAll(size: Int, offset: Int): List<EventDataEntity>

    @Query("SELECT * FROM ${EntityConstants.TABLE_NAME_EVENT}")
    fun getPagingSource(): PagingSource<Int, EventDataEntity>

    @Query("DELETE FROM ${EntityConstants.TABLE_NAME_EVENT}")
    suspend fun clearEvents()
}