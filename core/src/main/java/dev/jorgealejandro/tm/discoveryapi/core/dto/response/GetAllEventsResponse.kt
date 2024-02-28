package dev.jorgealejandro.tm.discoveryapi.core.dto.response

import com.google.gson.annotations.SerializedName
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventImageEntity

data class GetAllEventsResponse(
    @field:SerializedName("_embedded") val embedded: EventEmbeddedDataResponse? = null,
    @field:SerializedName("_links") val links: EventLinksResponse? = null,
    @field:SerializedName("page") val page: EventPageResponse? = null
)

//region embedded
data class EventEmbeddedDataResponse(
    @field:SerializedName("events") val events: List<EventResponse>? = null
)

data class EventResponse(
    @field:SerializedName("id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("type") val type: String? = null,
    @field:SerializedName("test") val test: Boolean? = null,
    @field:SerializedName("url") val url: String? = null,
    @field:SerializedName("info") val info: String? = null,
    @field:SerializedName("locale") val locale: String? = null,
    @field:SerializedName("images") val images: List<ImageEventResponse> = listOf(),
    @field:SerializedName("dates") val dates: DateEventResponse? = null,
    @field:SerializedName("_embedded") val _embedded: EventEmbeddedResponse? = null,
) {
    val toDomain: EventDataEntity
        get() = EventDataEntity(
            id = this.id ?: "-",
            name = this.name,
            type = this.type,
            url = this.url,
            info = this.info,
            locale = this.locale,
            date = this.dates?.start?.dateTime,
            venue = _embedded?.venues?.get(0)?.name,
            location = _embedded?.venues?.get(0)?.city?.name + " | " +
                    _embedded?.venues?.get(0)?.state?.name + ", " +
                    _embedded?.venues?.get(0)?.state?.stateCode,
            images = this.images.map { image -> image.toDomain }
        )
}

data class ImageEventResponse(
    @field:SerializedName("ratio") val ratio: String? = null,
    @field:SerializedName("url") val url: String? = null,
    @field:SerializedName("width") val width: Long,
    @field:SerializedName("height") val height: Long,
    @field:SerializedName("fallback") val fallback: Boolean
) {
    val toDomain: EventImageEntity
        get() = EventImageEntity(
            ratio = this.ratio,
            url = this.url,
            width = this.width,
            height = this.height,
            fallback = this.fallback
        )
}

data class DateEventResponse(
    @field:SerializedName("start") val start: DateEventStartResponse? = null,
    @field:SerializedName("timezone") val url: String? = null,
    @field:SerializedName("spanMultipleDays") val spanMultipleDays: Boolean,
)

data class DateEventStartResponse(
    @field:SerializedName("localDate") val localDate: String? = null,
    @field:SerializedName("localTime") val localTime: String? = null,
    @field:SerializedName("dateTime") val dateTime: String? = null,
    @field:SerializedName("dateTBD") val dateTBD: Boolean,
    @field:SerializedName("dateTBA") val dateTBA: Boolean,
)

data class EventEmbeddedResponse(
    @field:SerializedName("venues") val venues: List<VenuesResponse>? = null
)

data class VenuesResponse(
    @field:SerializedName("id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("type") val type: String? = null,
    @field:SerializedName("aliases") val aliases: List<String>? = null,
    @field:SerializedName("postalCode") val postalCode: String? = null,
    @field:SerializedName("timezone") val timezone: String? = null,
    @field:SerializedName("city") val city: CityVenueResponse? = null,
    @field:SerializedName("state") val state: StateVenueResponse? = null,
    @field:SerializedName("country") val country: CountryVenueResponse? = null
)

data class CityVenueResponse(@field:SerializedName("name") val name: String? = null)

data class StateVenueResponse(
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("stateCode") val stateCode: String? = null
)

data class CountryVenueResponse(
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("countryCode") val countryCode: String? = null
)
//endregion embedded

//region links
data class EventLinksResponse(
    @field:SerializedName("first") val first: LinkReferenceResponse? = null,
    @field:SerializedName("self") val self: LinkReferenceResponse? = null,
    @field:SerializedName("next") val next: LinkReferenceResponse? = null,
    @field:SerializedName("last") val last: LinkReferenceResponse? = null
)

data class LinkReferenceResponse(
    @field:SerializedName("href") val href: String? = null
)
//endregion links

//region page
data class EventPageResponse(
    @field:SerializedName("size") val size: Long? = null,
    @field:SerializedName("totalElements") val totalElements: Long? = null,
    @field:SerializedName("totalPages") val totalPages: Long? = null,
    @field:SerializedName("number") val number: Long? = null
)
//endregion page