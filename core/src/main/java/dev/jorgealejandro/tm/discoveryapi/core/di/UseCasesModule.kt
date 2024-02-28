package dev.jorgealejandro.tm.discoveryapi.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.UseCases
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetAllEventsUseCase
import dev.jorgealejandro.tm.discoveryapi.core.interactors.usecase.impl.GetCachedEventsUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetAllEventsUseCase(
        impl: EventsRepository
    ): UseCases.GetAllEvents = GetAllEventsUseCase(impl)

    @Provides
    fun provideCachedEventsUseCase(
        impl: EventsRepository
    ): UseCases.GetCachedEvents = GetCachedEventsUseCase(impl)
}