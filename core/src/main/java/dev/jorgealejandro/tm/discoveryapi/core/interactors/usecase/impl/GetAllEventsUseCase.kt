package dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BaseParams
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ApiConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.Extra
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.keyExtra
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.GetAllEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val repository: EventsRepository
) : GetAllEvents {
    @ExperimentalPagingApi
    override suspend operator fun invoke(params: Params): Flow<PagingData<EventDto>> =
        flow {
            emitAll(repository.getPagedFetchedEvents(params))
        }

    data class Params(
        val queries: List<Pair<keyExtra, Extra>> = ApiConstants.GET_EVENTS.queryParams
    ) : BaseParams
}