package dev.jorgealejandro.tm.discoveryapi.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants

@Entity(tableName = EntityConstants.TABLE_NAME_REMOTE_KEYS)
data class RemoteKeysEntity(
    @PrimaryKey val eventId: String,
    val prevKey: Int?,
    val nextKey: Int?
)