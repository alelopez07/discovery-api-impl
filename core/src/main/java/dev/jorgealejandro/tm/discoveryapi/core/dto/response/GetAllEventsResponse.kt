package dev.jorgealejandro.tm.discoveryapi.core.dto.response

import com.google.gson.annotations.SerializedName

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
    @field:SerializedName("locale") val locale: String? = null,
    @field:SerializedName("images") val images: List<ImageEventResponse>? = null,
)

data class ImageEventResponse(
    @field:SerializedName("ratio") val ratio: String? = null,
    @field:SerializedName("url") val url: String? = null,
    @field:SerializedName("width") val width: Long? = null,
    @field:SerializedName("height") val height: Long? = null,
    @field:SerializedName("fallback") val fallback: Boolean? = null
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