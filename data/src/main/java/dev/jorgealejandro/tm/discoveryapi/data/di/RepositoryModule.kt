package dev.jorgealejandro.tm.discoveryapi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jorgealejandro.tm.discoveryapi.core.interactors.repository.EventsRepository
import dev.jorgealejandro.tm.discoveryapi.data.local.AppDatabase
import dev.jorgealejandro.tm.discoveryapi.data.networking.DiscoveryApi
import dev.jorgealejandro.tm.discoveryapi.data.repository.EventsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideEventsRepository(
        api: DiscoveryApi,
        database: AppDatabase
    ): EventsRepository = EventsRepositoryImpl(api, database)
}