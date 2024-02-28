package dev.jorgealejandro.tm.discoveryapi.challenge.ui.states

import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ConnectionStateConstants

data class HomeUiState(
    val isLoading: Boolean = false,
    val connectionStatus: ConnectionStateConstants = ConnectionStateConstants.INACTIVITY
)