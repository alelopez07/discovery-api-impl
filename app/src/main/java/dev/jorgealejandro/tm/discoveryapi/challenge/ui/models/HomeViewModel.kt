package dev.jorgealejandro.tm.discoveryapi.challenge.ui.models

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.base.BaseViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.events.HomeUiEvents
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.DEFAULT_QUERY
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.HomeUiAction
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.HomeUiState
import dev.jorgealejandro.tm.discoveryapi.challenge.util.UIEvent
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ConnectionStateConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.UseCases
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase
import dev.jorgealejandro.tm.discoveryapi.data.utils.NetworkConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchEvents: UseCases.GetAllEvents,
    private val cachedEvents: UseCases.GetCachedEvents,
    private val network: NetworkConnection
) : BaseViewModel() {

    private val state = MutableStateFlow(HomeUiState())

    val action: (HomeUiAction) -> Unit
    var uiState: StateFlow<HomeUiState> = state
    var pagingEventsFlow: Flow<PagingData<EventUiItem>>? = null
    var lastQueryScrolled = MutableStateFlow(DEFAULT_QUERY)

    init {
        val actionStateFlow = MutableSharedFlow<HomeUiAction>()

        val searchEvents = actionStateFlow
            .filterIsInstance<HomeUiAction.SearchAction>()
            .distinctUntilChanged()
            .onStart { emit(HomeUiAction.SearchAction(query = DEFAULT_QUERY)) }

        val queriesOnScroll = actionStateFlow
            .filterIsInstance<HomeUiAction.ScrollAction>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            ).onStart { emit(HomeUiAction.ScrollAction(query = lastQueryScrolled.value)) }

        pagingEventsFlow = searchEvents.flatMapLatest { act ->
            processingEvents(query = act.query)
        }.cachedIn(viewModelScope)

        uiState = combine(
            searchEvents,
            queriesOnScroll,
            ::Pair
        ).map { (search, scroll) ->
            HomeUiState(
                query = search.query,
                isLoading = false,
                lastQueryScrolled = scroll.query,
                hasNotScrolledForCurrentSearch = search.query != scroll.query,
                connectionStatus = getConnectionStatus()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = HomeUiState()
        )

        action = { x ->
            viewModelScope.launch { actionStateFlow.emit(x) }
        }
    }

    val onUiAction: (HomeUiAction) -> Unit = { action ->
        if (action is HomeUiAction.SearchAction) {
            val newQuery = action.query
            state.update { _ ->
                HomeUiState(
                    query = newQuery,
                    isLoading = state.value.isLoading,
                    connectionStatus = getConnectionStatus()
                )
            }
        }
    }

    private fun getConnectionStatus() =
        if (network.networkAccess()) {
            ConnectionStateConstants.ONLINE
        } else {
            ConnectionStateConstants.OFFLINE
        }

    override fun onUIEvent(event: UIEvent) {
        when (event) {
            is HomeUiEvents.OnInitializeContent -> Unit// processingContent()
            else -> Unit
        }
    }

    private fun processingEvents(query: String? = null): Flow<PagingData<EventUiItem>> =
        fetchEvents.invoke(GetAllEventsUseCase.Params(keyword = query)).map { pagingData ->
            pagingData.map { event ->
                EventUiItem.EventItem(item = event.toDto)
            }.insertSeparators { before, after ->
                when {
                    before == null -> EventUiItem.Separator("HEADER")
                    after == null -> EventUiItem.Separator("FOOTER")
                    before.item != after.item -> EventUiItem.Separator(
                        "Between items $before and $after"
                    )

                    else -> EventUiItem.Separator("unexpected.")
                }
            }
        }

    private fun processingContent(query: String? = null) {
        exec {
            val params = GetAllEventsUseCase.Params(keyword = query)
            pagingEventsFlow = fetchEvents.invoke(params).map { pagingData ->
                pagingData.map { event ->
                    EventUiItem.EventItem(item = event.toDto)
                }.insertSeparators { before, after ->
                    when {
                        before == null -> EventUiItem.Separator("HEADER")
                        after == null -> EventUiItem.Separator("FOOTER")
                        before.item != after.item -> EventUiItem.Separator(
                            "Between items $before and $after"
                        )

                        else -> null
                    }
                }
            }.cachedIn(viewModelScope)
        }
    }
}
