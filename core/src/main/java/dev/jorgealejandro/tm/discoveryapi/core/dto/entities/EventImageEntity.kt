package dev.jorgealejandro.tm.discoveryapi.core.dto.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants

@Entity(tableName = EntityConstants.TABLE_NAME_EVENT_IMAGE)
data class EventImageEntity(
    @PrimaryKey(autoGenerate = false) val id: Long? = 0,
    @ColumnInfo(name = "ratio") val ratio: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "width") val width: Long,
    @ColumnInfo(name = "height") val height: Long,
    @ColumnInfo(name = "fallback") val fallback: Boolean
)