package dev.jorgealejandro.tm.discoveryapi.challenge.ui.states

import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ConnectionStateConstants

data class HomeUiState(
    val query: String? = DEFAULT_QUERY,
    val isLoading: Boolean = false,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false,
    val connectionStatus: ConnectionStateConstants = ConnectionStateConstants.INACTIVITY
)

const val DEFAULT_QUERY = ""