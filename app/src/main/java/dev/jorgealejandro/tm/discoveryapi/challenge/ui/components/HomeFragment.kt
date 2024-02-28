package dev.jorgealejandro.tm.discoveryapi.challenge.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import dev.jorgealejandro.tm.discoveryapi.challenge.databinding.FragmentHomeBinding
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.EventLoadStateAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.EventsAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.events.HomeUiEvents
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.models.HomeViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.DataPresentationState
import dev.jorgealejandro.tm.discoveryapi.challenge.util.asRemotePresentationState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onUIEvent(HomeUiEvents.OnInitializeContent)
        bindComponents()
    }

    private fun bindComponents() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                // To handle the UI $state
            }
        }

        val eventAdapter = EventsAdapter(viewModel)
        val header = EventLoadStateAdapter { eventAdapter.retry() }
        binding.eventList.adapter = eventAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = EventLoadStateAdapter { eventAdapter.retry() }
        )

        val notLoading = eventAdapter
            .loadStateFlow
            .asRemotePresentationState()
            .map { it == DataPresentationState.PRESENTED }

        lifecycleScope.launch {
            viewModel.pagingEventsFlow?.collectLatest(eventAdapter::submitData)
        }

        lifecycleScope.launch {
            eventAdapter.loadStateFlow.collectLatest { state ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = state.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && eventAdapter.itemCount > 0 }
                    ?: state.prepend

                binding.eventList.isVisible = state.source.refresh is LoadState.NotLoading ||
                        state.mediator?.refresh is LoadState.NotLoading

                // Toast on any error, regardless of whether it came from
                // RemoteMediator or PagingSource.
                (state.source.append as? LoadState.Error
                    ?: state.source.prepend as? LoadState.Error
                    ?: state.append as? LoadState.Error
                    ?: state.prepend as? LoadState.Error)?.let { errorState ->
                    Toast.makeText(
                        context,
                        "Something went wrong! | ${errorState.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // -> When the list is empty
                // binding.textEmptyList = state.refresh is LoadState.NotLoading && eventAdapter.itemCount == 0

                // -> Show loading spinner (progress) during initial load or refresh.
                // binding.progress.isVisible = state.mediator?.refresh is LoadState.Loading

                // -> Show the retry state if initial load or refresh fails
                // binding.buttonRetry.isVisible = state.mediator?.refresh is LoadState.Error && eventAdapter.itemCount == 0
            }
        }
    }
}