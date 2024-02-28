package dev.jorgealejandro.tm.discoveryapi.core.dto.constants

typealias keyExtra = String
typealias Extra = String

enum class ApiConstants(
    val purpose: String,
    val ep: String,
    val queryParams: List<Pair<keyExtra, Extra>> = listOf(),
    val errorMessage: String = "Something went wrong" // For all unexpected scenarios.
) {
    GET_EVENTS(
        purpose = "Get a list of all events in the United States.",
        ep = "events.json",
        queryParams = listOf(
            Pair("countryCode", "US")
        )
    ),
    SEARCH_FOR_EVENTS(
        purpose = "Search for events sourced by Universe in the United States with keyword",
        ep = "events.json",
        queryParams = listOf(
            Pair("countryCode", "US"),
            Pair("source", "universe"),
            Pair("keyword", "optional")
        )
    )
}