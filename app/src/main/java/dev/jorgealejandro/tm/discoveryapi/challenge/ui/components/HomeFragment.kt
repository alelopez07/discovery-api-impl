package dev.jorgealejandro.tm.discoveryapi.challenge.ui.components

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.jorgealejandro.tm.discoveryapi.challenge.databinding.FragmentHomeBinding
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.EventLoadStateAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.adapters.EventsAdapter
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.events.HomeUiEvents
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.models.HomeViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.DataPresentationState
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.HomeUiAction
import dev.jorgealejandro.tm.discoveryapi.challenge.util.asRemotePresentationState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
        bindComponents(onQueryChanged = viewModel.action)
        bindObservables(onScrollChanged = viewModel.action)
    }

    private fun bindComponents(onQueryChanged: (HomeUiAction.SearchAction) -> Unit) {
        binding.textSearchEvent.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                refreshEventsFromSearch(onQueryChanged)
                true
            } else {
                false
            }
        }

        binding.textSearchEvent.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                refreshEventsFromSearch(onQueryChanged)
                true
            } else {
                false
            }
        }

        binding.eventList.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(binding.textSearchEvent::setText)
        }
    }

    private fun bindObservables(
        onScrollChanged: (HomeUiAction.ScrollAction) -> Unit
    ) {
        val eventAdapter = EventsAdapter(viewModel)
        val header = EventLoadStateAdapter { eventAdapter.retry() }

        binding.eventList.adapter = eventAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = EventLoadStateAdapter { eventAdapter.retry() }
        )

        binding.eventList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    viewModel.uiState.value.query?.let {
                        onScrollChanged(HomeUiAction.ScrollAction(query = it))
                    }
                }
            }
        })

        val isNotLoading = eventAdapter.loadStateFlow
            .asRemotePresentationState().map {
                value -> value == DataPresentationState.REMOTE_LOADING
            }

        val hasNotScrolledForCurrentSearch = viewModel.uiState.map {
            it.hasNotScrolledForCurrentSearch
        }.distinctUntilChanged()

        val shouldScrollToTop = combine(
            isNotLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()


        lifecycleScope.launch {
            viewModel.pagingEventsFlow?.collectLatest(eventAdapter::submitData)
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) binding.eventList.scrollToPosition(0)
            }
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

                // -> Show loading spinner (progress) during initial load or refresh.
                binding.progressBarEventsContent.isVisible =
                    state.mediator?.refresh is LoadState.Loading

                // -> Show the retry state if initial load or refresh fails
                binding.buttonRetryNoEvents.isVisible =
                    state.mediator?.refresh is LoadState.Error && eventAdapter.itemCount == 0

                // -> When the list is empty
                binding.textEmptyEventList.isVisible =
                    state.refresh is LoadState.NotLoading && eventAdapter.itemCount == 0
            }
        }
    }

    private fun refreshEventsFromSearch(onQueryChanged: (HomeUiAction.SearchAction) -> Unit) {
        binding.textSearchEvent.text.trim().let { query ->
            if (query.isNotEmpty()) {
                binding.eventList.scrollToPosition(0)
                onQueryChanged(
                    HomeUiAction.SearchAction(
                        query = query.toString().trim()
                    )
                )
            }
        }
    }
}