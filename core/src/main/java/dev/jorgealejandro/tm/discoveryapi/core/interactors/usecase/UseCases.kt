package dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase

import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BasePagedUseCase
import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BaseParams
import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BaseUseCase
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventDataEntity
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase

/**
 * [UseCases]
 * Object that centralizes the use cases.
 *
 * Each use case is represented by an interface that extends
 * the [BasePagedUseCase]/[BaseUseCase] class.
 *
 * This allows for a consistent and structured approach to defining and implementing use cases.
 * By using this object, you can easily access and use the use cases in your ViewModels
 * or in your injection dependency structure.
 */
interface UseCases {
    /**
     * [UseCases.GetAllEvents] - Obtain all the updated elements and place them in a
     * paginated structure.
     */
     interface GetAllEvents: BasePagedUseCase<GetAllEventsUseCase.Params, EventDataEntity>

    /**
     * [UseCases.GetCachedEvents] - Getting all the cached events and paginate it.
     */
    interface GetCachedEvents : BasePagedUseCase<BaseParams, EventDto>
}