package dev.jorgealejandro.tm.discoveryapi.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ApiConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
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
    override fun getPagedFetchedEvents(params: GetAllEventsParams):
            Flow<PagingData<EventDataEntity>> {
        val origin = ApiConstants.GET_EVENTS
        var queries = params.queries.joinToString(separator = "&") { (key, value) ->
            "$key=$value"
        }

        var customSource = { db.eventDao().getPagingSource() }

        params.keyword?.let { query ->
            queries += "&keyword=$query"
            customSource = { db.eventDao().eventsByName(query) }
        }

        val mediator: RemoteMediator<Int, EventDataEntity> =
            EventDataMediator(origin, queries, api, db)

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2,
                prefetchDistance = PAGE_SIZE * 3,
                enablePlaceholders = true
            ),
            remoteMediator = mediator,
            pagingSourceFactory = customSource
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getLocalPagedEvents(): Flow<PagingData<EventDto>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            EventPagingSource(db)
        }.flow
    }
}