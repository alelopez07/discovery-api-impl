package dev.jorgealejandro.tm.discoveryapi.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ApiConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.data.local.AppDatabase
import dev.jorgealejandro.tm.discoveryapi.data.networking.DiscoveryApi
import dev.jorgealejandro.tm.discoveryapi.data.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase.Params as GetAllEventsParams

class EventsRepositoryImpl @Inject constructor(
    private val api: DiscoveryApi,
    private val db: AppDatabase
) : EventsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagedFetchedEvents(params: GetAllEventsParams):
            Flow<PagingData<EventDto>> {

        val origin = ApiConstants.GET_EVENTS
        val queries = params.queries.joinToString(separator = "&") { (key, value) ->
            "$key=$value"
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = EventDataMediator(
                origin,
                queries,
                api,
                db
            )
        ) {
            EventPagingSource(db)
        }.flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getLocalPagedEvents(): Flow<PagingData<EventDto>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            EventPagingSource(db)
        }.flow
    }
}