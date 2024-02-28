package dev.jorgealejandro.tm.discoveryapi.core.dto.constants

enum class ConnectionStateConstants(val message: String) {
    OFFLINE(message = "No internet connection"),
    ONLINE(message = "You are online"),
    INACTIVITY(message = "Inactivity.")
}