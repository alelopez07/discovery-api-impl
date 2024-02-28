package dev.jorgealejandro.tm.discoveryapi.core.dto.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EntityConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto

@Entity(tableName = EntityConstants.TABLE_NAME_EVENT)
data class EventDataEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "info") val info: String?,
    @ColumnInfo(name = "locale") val locale: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "venue") val venue: String?,
    @ColumnInfo(name = "location") val location: String?,
    val images: List<EventImageEntity> = listOf()
) {
    val toDto: EventDto
        get() = EventDto(
            id = this.id,
            name = this.name,
            type = this.type,
            url = this.url,
            locale = this.locale,
            imagePreview = images.random().url,
            date = this.date,
            venue = this.venue,
            location = this.location
        )
}