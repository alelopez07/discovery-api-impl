package dev.jorgealejandro.tm.discoveryapi.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto

@Entity(tableName = EntityConstants.TABLE_NAME_EVENT)
data class EventEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "test") val test: Boolean?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "locale") val locale: String?
)

val List<EventEntity>.toDto: List<EventDto>
    get() = this.map {
        EventDto(
            id = it.id,
            name = it.name,
            type = it.type,
            url = it.url,
            test = it.test,
            locale = it.locale
        )
    }