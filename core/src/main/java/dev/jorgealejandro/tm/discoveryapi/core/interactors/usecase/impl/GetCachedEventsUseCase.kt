package dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl

import androidx.paging.PagingData
import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BaseParams
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.UseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetCachedEventsUseCase(
    private val repository: EventsRepository
) : UseCases.GetCachedEvents {
    override suspend fun invoke(params: BaseParams): Flow<PagingData<EventDto>> =
        flow {
            emitAll(repository.getLocalPagedEvents())
        }
}