package dev.jorgealejandro.tm.discoveryapi.challenge.ui.events

import dev.jorgealejandro.tm.discoveryapi.challenge.util.UIEvent

sealed class HomeUiEvents : UIEvent {
    data object OnInitializeContent : HomeUiEvents()
}