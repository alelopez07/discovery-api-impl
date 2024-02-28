package dev.jorgealejandro.tm.discoveryapi.core.dto.models

/**
 * [EventUiItem]
 * Sealed class representing the different types of items that can be displayed in the UI for
 * events (in a pagination context).
 * It contains two subclasses:
 *
 * [EventUiItem.EventItem]: Represents an individual event.
 * [EventUiItem.Separator]: Represents a separator between different sections of events.
 */
sealed class EventUiItem {

    /**
     * [EventUiItem.EventItem] Class representing an individual event.
     *
     * @param item The DTO containing the event previews.
     */
    data class EventItem(val item: EventDto) : EventUiItem()

    /**
     * [EventUiItem.Separator] Class representing a separator between different sections of events.
     *
     * @param description (optional) The description of the separator. Defaults to an empty string.
     */
    data class Separator(val description: String? = "") : EventUiItem()
}