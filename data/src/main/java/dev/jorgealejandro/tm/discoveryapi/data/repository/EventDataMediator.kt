package dev.jorgealejandro.tm.discoveryapi.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ApiConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.data.local.AppDatabase
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.RemoteKeysEntity
import dev.jorgealejandro.tm.discoveryapi.data.networking.DiscoveryApi
import dev.jorgealejandro.tm.discoveryapi.data.utils.STARTING_PAGE_INDEX
import dev.jorgealejandro.tm.discoveryapi.data.utils.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class EventDataMediator(
    private val origin: ApiConstants,
    private val queries: String,
    private val api: DiscoveryApi,
    private val db: AppDatabase
) : RemoteMediator<Int, EventDto>() {
    override suspend fun initialize(): InitializeAction {
        // In some cases, if it's acceptable to show slightly outdated cached data,
        // you can return SKIP_INITIAL_REFRESH instead of triggering the remote refresh.
        // This allows the paging to proceed without waiting for the initial refresh.

        // However, it advises not to trigger remote prepend (loading previous pages) or append
        // (loading next pages) until the initial remote refresh has succeeded.
        // This ensures that the data being displayed is up-to-date.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventDto>
    ): MediatorResult {
        val pageKeyData = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
        }

        val page = when (pageKeyData) {
            is MediatorResult.Success -> STARTING_PAGE_INDEX
            else -> pageKeyData as Int
        }

        val url = origin.ep + "?" + queries

        try {
            val response = api.getAllEvents(url = url, page = page)
            if (response.isSuccessful) {
                val data = response.body()
                val events = data?.embedded?.events
                val endOfPaginationReached = events?.isEmpty() ?: false

                CoroutineScope(Dispatchers.IO).launch {
                    if (loadType === LoadType.REFRESH) {
                        db.eventDao().clearEvents()
                        db.remoteKeysDao().deleteAll()
                    }

                    val prev = if (page == STARTING_PAGE_INDEX) null else page - 1
                    val next = if (endOfPaginationReached) null else page + 1

                    events?.toDomain?.let { items ->
                        db.eventDao().saveEvents(items)
                    }

                    events?.map { result ->
                        RemoteKeysEntity(
                            eventId = result.id ?: "-",
                            prevKey = prev,
                            nextKey = next
                        )
                    }?.let { keys ->
                        db.remoteKeysDao().insertAll(keys)
                    }
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            return MediatorResult.Error(
                throwable = Throwable(origin.errorMessage)
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, EventDto>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { event ->
            db.remoteKeysDao().remoteKeysEventId(event.id)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EventDto>): RemoteKeysEntity? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { event ->
            db.remoteKeysDao().remoteKeysEventId(event.id)
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EventDto>): RemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { event ->
            db.remoteKeysDao().remoteKeysEventId(event.id)
        }
    }
}