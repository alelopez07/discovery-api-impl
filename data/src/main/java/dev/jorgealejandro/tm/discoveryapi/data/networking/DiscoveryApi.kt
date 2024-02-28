package dev.jorgealejandro.tm.discoveryapi.data.networking

import dev.jorgealejandro.tm.discoveryapi.core.dto.response.GetAllEventsResponse
import dev.jorgealejandro.tm.discoveryapi.data.utils.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface DiscoveryApi {
    @GET
    suspend fun getAllEvents(
        @Url url: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = PAGE_SIZE
    ): Response<GetAllEventsResponse>
}