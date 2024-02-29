package dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BaseParams
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ApiConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.Extra
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.keyExtra
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.UseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val repository: EventsRepository
) : UseCases.GetAllEvents {
    @ExperimentalPagingApi
    override operator fun invoke(params: Params): Flow<PagingData<EventDataEntity>> =
        flow {
            emitAll(repository.getPagedFetchedEvents(params))
        }

    data class Params(
        val keyword: String? = null,
        val queries: List<Pair<keyExtra, Extra>> = ApiConstants.GET_EVENTS.queryParams
    ) : BaseParams
}