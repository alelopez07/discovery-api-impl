package dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import dev.jorgealejandro.tm.discoveryapi.challenge.R
import dev.jorgealejandro.tm.discoveryapi.challenge.databinding.ItemLoadStateBinding

class EventLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.buttonRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(state: LoadState) {
        if (state is LoadState.Error) {
            binding.textErrorMessage.text = state.error.localizedMessage
        }

        binding.apply {
            progress.isVisible = state is LoadState.Loading
            buttonRetry.isVisible = state is LoadState.Error
            textErrorMessage.isVisible = state is LoadState.Error
        }
    }

    companion object {
        fun attach(
            parent: ViewGroup,
            retry: () -> Unit
        ): EventLoadStateViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_load_state, parent, false)
            val binding = ItemLoadStateBinding.bind(view)
            return EventLoadStateViewHolder(binding, retry)
        }
    }
}