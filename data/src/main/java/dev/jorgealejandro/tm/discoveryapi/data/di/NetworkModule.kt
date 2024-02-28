package dev.jorgealejandro.tm.discoveryapi.data.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.NetworkFactoryConstants
import dev.jorgealejandro.tm.discoveryapi.data.BuildConfig
import dev.jorgealejandro.tm.discoveryapi.data.networking.DiscoveryApi
import dev.jorgealejandro.tm.discoveryapi.data.utils.LAYER
import dev.jorgealejandro.tm.discoveryapi.data.utils.NetworkConnection
import dev.jorgealejandro.tm.discoveryapi.data.utils.NetworkConnectionImpl
import dev.jorgealejandro.tm.discoveryapi.data.utils.TIME_OUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideNetworkConnection(
        @ApplicationContext ctx: Context
    ): NetworkConnection = NetworkConnectionImpl(ctx)

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val networkLogging = HttpLoggingInterceptor { message ->
            Timber.tag(LAYER).d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerAuthorizationInterceptor = Interceptor { chain ->
            val request = chain.request()

            // Build the url with the query parameter: apiKey.
            val url = request.url
                .newBuilder()
                .addQueryParameter(
                    NetworkFactoryConstants.API_KEY.content,
                    BuildConfig.API_KEY
                ).build()

            val builder = request.newBuilder()
                .addHeader(
                    name = NetworkFactoryConstants.CONTENT_TYPE_KEY.content,
                    value = NetworkFactoryConstants.JSON_HEADER_TYPE_VALUE.content
                )
                .addHeader(
                    name = NetworkFactoryConstants.ACCEPT_KEY.content,
                    value = NetworkFactoryConstants.JSON_HEADER_TYPE_VALUE.content
                )
                .url(url)

            chain.proceed(builder.build())
        }

        client.interceptors().add(headerAuthorizationInterceptor)
        client.apply {
            addNetworkInterceptor(networkLogging)
            addNetworkInterceptor(networkLogging)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        }

        return client.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): DiscoveryApi = retrofit.create(DiscoveryApi::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}