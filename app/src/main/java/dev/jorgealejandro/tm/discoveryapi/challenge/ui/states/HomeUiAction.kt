package dev.jorgealejandro.tm.discoveryapi.challenge.ui.states

sealed class HomeUiAction {
    data class SearchAction(val query: String): HomeUiAction()
    data class ScrollAction(val query: String): HomeUiAction()
    data object NA : HomeUiAction()
}