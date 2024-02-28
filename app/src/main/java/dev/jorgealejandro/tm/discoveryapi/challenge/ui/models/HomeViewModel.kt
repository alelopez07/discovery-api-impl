package dev.jorgealejandro.tm.discoveryapi.challenge.ui.models

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.base.BaseViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.events.HomeUiEvents
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.HomeUiState
import dev.jorgealejandro.tm.discoveryapi.challenge.util.UIEvent
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ConnectionStateConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventUiItem
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.GetAllEvents
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
    private val fetchEvents: GetAllEvents,
    private val cachedEvents: UseCases.GetCachedEvents,
    private val network: NetworkConnection
) : BaseViewModel() {

    var pagingEventsFlow: Flow<PagingData<EventUiItem>>? = null

    private val state = MutableStateFlow(HomeUiState())
    var uiState: StateFlow<HomeUiState> = state

    init {
        state.update { _ ->
            HomeUiState(
                isLoading = true,
                connectionStatus =
                if (network.networkAccess()) {
                    ConnectionStateConstants.ONLINE
                } else {
                    ConnectionStateConstants.OFFLINE
                }
            )
        }
    }

    override fun onUIEvent(event: UIEvent) {
        when (event) {
            is HomeUiEvents.OnInitializeContent -> processingContent()
            else -> Unit
        }
    }

    private fun processingContent() {
        exec {
            pagingEventsFlow = fetchEvents.invoke(
                GetAllEventsUseCase.Params()
            ).map { pagingData ->
                pagingData.map { dto ->
                    EventUiItem.EventItem(item = dto)
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