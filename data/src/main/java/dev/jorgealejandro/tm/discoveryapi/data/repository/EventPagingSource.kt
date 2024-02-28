package dev.jorgealejandro.tm.discoveryapi.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.data.local.AppDatabase
import dev.jorgealejandro.tm.discoveryapi.data.local.entities.toDto
import dev.jorgealejandro.tm.discoveryapi.data.utils.STARTING_PAGE_INDEX

class EventPagingSource(
    private val db: AppDatabase
) : PagingSource<Int, EventDto>() {
    override fun getRefreshKey(state: PagingState<Int, EventDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(STARTING_PAGE_INDEX)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(STARTING_PAGE_INDEX)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventDto> {
        val page = params.key ?: STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        val offset = page * pageSize
        return try {
            val response = db.eventDao().getAll(pageSize, offset).toDto
            LoadResult.Page(
                data = response,
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if (response.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}