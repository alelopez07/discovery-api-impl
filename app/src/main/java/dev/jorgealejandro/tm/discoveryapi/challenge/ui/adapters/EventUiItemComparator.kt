package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem

class EventUiItemComparator : DiffUtil.ItemCallback<EventUiItem>() {
    override fun areItemsTheSame(
        old: EventUiItem,
        new: EventUiItem
    ): Boolean {
        return (old is EventUiItem.EventItem && new is EventUiItem.EventItem &&
                old.item.id == new.item.id) || (old is EventUiItem.Separator &&
                new is EventUiItem.Separator && old.description == new.description)
    }

    override fun areContentsTheSame(old: EventUiItem, new: EventUiItem): Boolean =
        old == new
}