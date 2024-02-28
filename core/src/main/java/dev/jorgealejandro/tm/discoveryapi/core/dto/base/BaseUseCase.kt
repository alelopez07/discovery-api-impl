package dev.jorgealejandro.tm.discoveryapi.core.dto.base

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<Params : BaseParams, Output> {
    suspend operator fun invoke(params: Params): Flow<Resource<Output>> =
        throw NotImplementedError()
}