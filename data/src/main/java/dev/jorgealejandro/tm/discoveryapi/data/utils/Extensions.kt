package dev.jorgealejandro.tm.discoveryapi.data.utils

import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto

val List<EventDataEntity>.toDto: List<EventDto>
    get() = this.map { entity ->
        EventDto(
            id = entity.id,
            name = entity.name,
            type = entity.type,
            url = entity.url,
            locale = entity.locale,
            imagePreview = entity.images.random().url,
            date = entity.date,
            venue = entity.venue,
            location = entity.location
        )
    }