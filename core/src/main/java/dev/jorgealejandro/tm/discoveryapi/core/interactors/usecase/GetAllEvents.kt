package dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase

import dev.jorgealejandro.tm.discoveryapi.core.dto.base.BasePagedUseCase
import dev.jorgealejandro.tm.discoveryapi.core.dto.models.EventDto
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase

interface GetAllEvents : BasePagedUseCase<GetAllEventsUseCase.Params, EventDto>