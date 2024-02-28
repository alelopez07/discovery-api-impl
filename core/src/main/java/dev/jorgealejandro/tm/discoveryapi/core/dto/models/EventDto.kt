package dev.jorgealejandro.tm.discoveryapi.core.dto.models


data class EventDto(
    val id: String,
    val name: String?,
    val type: String?,
    val test: Boolean?,
    val url: String?,
    val locale: String?
)