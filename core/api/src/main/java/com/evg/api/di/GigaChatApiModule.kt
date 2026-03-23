package com.evg.api.di

import com.evg.api.data.repository.GigaChatApiRepositoryImpl
import com.evg.api.domain.repository.GigaChatApiRepository
import com.evg.api.domain.service.GigaChatAuthService
import com.evg.api.domain.service.GigaChatService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GigaChatApiModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("GigaChatAuthRetrofit")
    fun provideGigaChatAuthRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ngw.devices.sberbank.ru:9443/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("GigaChatRetrofit")
    fun provideGigaChatRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gigachat.devices.sberbank.ru/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideGigaChatAuthService(
        @Named("GigaChatAuthRetrofit") retrofit: Retrofit,
    ): GigaChatAuthService {
        return retrofit.create(GigaChatAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideGigaChatService(
        @Named("GigaChatRetrofit") retrofit: Retrofit,
    ): GigaChatService {
        return retrofit.create(GigaChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideGigaChatApiRepository(
        gigaChatApiRepositoryImpl: GigaChatApiRepositoryImpl,
    ): GigaChatApiRepository {
        return gigaChatApiRepositoryImpl
    }
}
