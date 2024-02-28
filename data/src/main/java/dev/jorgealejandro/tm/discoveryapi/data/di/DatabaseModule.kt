package dev.jorgealejandro.tm.discoveryapi.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jorgealejandro.tm.discoveryapi.data.BuildConfig
import dev.jorgealejandro.tm.discoveryapi.data.local.AppDatabase
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.EventDao
import dev.jorgealejandro.tm.discoveryapi.data.local.dao.RemoteKeyDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()

    @Provides
    fun provideEventDao(database: AppDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: AppDatabase): RemoteKeyDao {
        return database.remoteKeysDao()
    }
}