package dev.jorgealejandro.tm.discoveryapi.core.interactors.repository

import androidx.paging.PagingData
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    suspend fun getPagedFetchedEvents(
        params: GetAllEventsUseCase.Params
    ): Flow<PagingData<EventDto>>

    suspend fun getLocalPagedEvents(): Flow<PagingData<EventDto>>
}