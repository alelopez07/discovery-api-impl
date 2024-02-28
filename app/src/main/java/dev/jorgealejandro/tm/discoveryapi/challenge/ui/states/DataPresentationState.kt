package dev.jorgealejandro.tm.discoveryapi.challenge.ui.states

/**
 * [DataPresentationState]
 *
 * This enum class represents the status of items in the adapter as fetched.
 */
enum class DataPresentationState {
    /**
     * [DataPresentationState.INITIAL] Initial state, before any data is loaded.
     */
    INITIAL,

    /**
     * [DataPresentationState.REMOTE_LOADING] Data is being loaded from the remote source.
     */
    REMOTE_LOADING,

    /**
     * [DataPresentationState.SOURCE_LOADING] Data is being loaded from the local source.
     */
    SOURCE_LOADING,

    /**
     * [DataPresentationState.PRESENTED] Data is ready to be presented.
     */
    PRESENTED
}