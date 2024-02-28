package dev.jorgealejandro.tm.discoveryapi.data.utils

import dev.jorgealejandro.tm.discoveryapi.core.dto.response.EventResponse
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.EventEntity

val List<EventResponse>.toDomain: List<EventEntity>
    get() = this.map { response ->
        EventEntity(
            id = response.id ?: "-",
            name = response.name,
            type = response.type,
            test = response.test,
            url = response.url,
            locale = response.locale
        )
    }