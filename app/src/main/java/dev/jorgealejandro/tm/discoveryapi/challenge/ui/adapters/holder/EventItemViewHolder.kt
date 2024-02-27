package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.jorgealejandro.tm.discoveryapi.challenge.base.BaseViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.databinding.ItemEventBinding
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.models.HomeViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.util.setImage
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem

class EventItemViewHolder(
    parent: ViewGroup,
    private val binding: ItemEventBinding = ItemEventBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
) : EventViewHolder(binding.root) {
    override fun bind(
        viewModel: BaseViewModel,
        item: EventUiItem?
    ) {
        if (viewModel !is HomeViewModel) return
        val model = (item as EventUiItem.EventItem)
        binding.textTitleEvent.text = model.item.name
        binding.textDateEvent.text = model.item.type
        binding.imageEvent.setImage(model.item.url)
    }
}