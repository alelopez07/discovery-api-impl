package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder.EventItemViewHolder
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder.EventViewHolder
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder.SeparatorItemViewHolder
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.models.HomeViewModel
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.EventItemViewTypeConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem
import java.lang.UnsupportedOperationException

class EventsAdapter(
    private val viewModel: HomeViewModel
) : PagingDataAdapter<EventUiItem, EventViewHolder>(EventUiItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return when (EventItemViewTypeConstants.entries[viewType]) {
            EventItemViewTypeConstants.ITEM_EVENT -> EventItemViewHolder(parent)
            EventItemViewTypeConstants.ITEM_SEPARATOR -> SeparatorItemViewHolder(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EventUiItem.EventItem -> EventItemViewTypeConstants.ITEM_EVENT.ordinal
            is EventUiItem.Separator -> EventItemViewTypeConstants.ITEM_SEPARATOR.ordinal
            null -> throw UnsupportedOperationException("unknown item view")
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}