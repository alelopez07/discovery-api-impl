package dev.jorgealejandro.tm.discoveryapi.core.dto.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface BasePagedUseCase<Params : BaseParams, Output : Any> {
    operator fun invoke(params: Params): Flow<PagingData<Output>> =
        throw NotImplementedError()
}