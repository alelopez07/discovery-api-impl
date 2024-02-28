package dev.jorgealejandro.tm.discoveryapi.core.dto.models

/**
 * [EventDto]
 * Represents the data structure for an event received from the backend.
 * It includes the following properties:
 *
 * - [id]: The unique identifier of the event.
 * - [name]: The name of the event.
 * - [type]: The type of the event.
 * - [url]: The URL associated with the event.
 * - [locale]: The locale or region of the event.
 * - [imagePreview]: An image preview for the item.
 * - [date]: The date of event.
 * - [venue]: The venue (alias) of the event.
 * - [location]: The location city|state of the event.
 */
data class EventDto(
    val id: String,
    val name: String?,
    val type: String?,
    val url: String?,
    val locale: String?,
    val imagePreview: String?,
    val date: String?,
    val venue: String?,
    val location: String?
)