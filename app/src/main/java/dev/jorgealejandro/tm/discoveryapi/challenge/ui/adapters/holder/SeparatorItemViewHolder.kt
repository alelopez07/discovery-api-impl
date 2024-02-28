package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.jorgealejandro.tm.discoveryapi.challenge.base.BaseViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.databinding.ItemSeparatorBinding
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder.EventViewHolder
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem

class SeparatorItemViewHolder(
    parent: ViewGroup,
    binding: ItemSeparatorBinding = ItemSeparatorBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
) : EventViewHolder(binding.root) {
    override fun bind(
        viewModel: BaseViewModel,
        item: EventUiItem?
    ) {

    }
}