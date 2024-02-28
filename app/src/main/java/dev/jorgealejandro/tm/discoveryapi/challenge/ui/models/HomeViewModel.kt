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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchEvents: UseCases.GetAllEvents,
    private val cachedEvents: UseCases.GetCachedEvents,
    private val network: NetworkConnection
) : BaseViewModel() {

    var pagingEventsFlow: Flow<PagingData<EventUiItem>>? = null

    private val state = MutableStateFlow(HomeUiState(uiAction = { HomeUiAction.NA }))
    var uiState: StateFlow<HomeUiState> = state

    init {
        state.update { _ ->
            HomeUiState(
                isLoading = true,
                connectionStatus = getConnectionStatus(),
                uiAction = { HomeUiAction.ScrollAction(DEFAULT_QUERY) }
            )
        }
    }

    val onUiAction: (HomeUiAction) -> Unit = { action ->
        if (action is HomeUiAction.SearchAction) {
            val newQuery = action.query
            processingContent(newQuery)
            state.update { _ ->
                HomeUiState(
                    isLoading = state.value.isLoading,
                    query = newQuery,
                    uiAction = { HomeUiAction.ScrollAction(newQuery) },
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
            is HomeUiEvents.OnInitializeContent -> processingContent()
            else -> Unit
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
