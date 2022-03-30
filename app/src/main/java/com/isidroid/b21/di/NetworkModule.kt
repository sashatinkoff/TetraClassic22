package com.isidroid.b21.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isidroid.b21.data.source.remote.AuthInterceptor
import com.isidroid.b21.data.source.remote.DateDeserializer
import com.isidroid.b21.data.source.remote.api.ApiTest
import com.isidroid.b21.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private fun <T> httpClient(
        cl: Class<T>,
        logLevel: HttpLoggingInterceptor.Level,
        authInterceptor: Interceptor?,
        readTimeOut: Long = 15,
        writeTimeOut: Long = 60
    ): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .readTimeout(readTimeOut, TimeUnit.SECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.SECONDS)

        authInterceptor?.let { builder.addInterceptor(it) }
        builder.addInterceptor(logger(cl = cl, logLevel = logLevel))

        return builder.build()
    }

    private fun <T> logger(cl: Class<T>, logLevel: HttpLoggingInterceptor.Level) =
        HttpLoggingInterceptor { message -> Timber.tag(cl.simpleName).i(message) }
            .apply { level = logLevel }

    @Provides
    fun provideAuthInterceptor(sessionRepository: SessionRepository) = AuthInterceptor(sessionRepository)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ")
        .registerTypeAdapter(Date::class.java, DateDeserializer())
        .create()

    private fun <T> api(
        baseUrl: String = "",
        cl: Class<T>,
        logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
        authInterceptor: Interceptor? = null,
        readTimeOut: Long = 15,
        writeTimeOut: Long = 60
    ): T = Retrofit.Builder()
        .client(
            httpClient(
                cl = cl,
                logLevel = logLevel,
                authInterceptor = authInterceptor,
                readTimeOut = readTimeOut,
                writeTimeOut = writeTimeOut
            )
        )
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(provideGson()))
        .build()
        .create(cl) as T

    @Singleton
    @Provides
    fun provideApiReports(interceptor: AuthInterceptor) = api(
        cl = ApiTest::class.java,
        logLevel = HttpLoggingInterceptor.Level.BODY,
        authInterceptor = interceptor
    )
}