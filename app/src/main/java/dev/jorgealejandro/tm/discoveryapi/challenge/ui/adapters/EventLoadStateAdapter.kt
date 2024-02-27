package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder.EventLoadStateViewHolder

class EventLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<EventLoadStateViewHolder>() {
    override fun onBindViewHolder(
        holder: EventLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): EventLoadStateViewHolder {
        return EventLoadStateViewHolder.attach(parent, retry)
    }
}