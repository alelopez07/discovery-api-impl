package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.jorgealejandro.tm.discoveryapi.challenge.base.BaseViewModel
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem

sealed class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(viewModel: BaseViewModel, item: EventUiItem?)
}