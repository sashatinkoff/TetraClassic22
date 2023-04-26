package com.isidroid.link_preview.di

import com.isidroid.link_preview.data.repository.OpenGraphMetaDataRepositoryImpl
import com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LinkPreviewModule {
    @Singleton @Provides
    fun provideOpenGraphMetaData(api: ApiLinkPreviewParser): OpenGraphMetaDataRepository = OpenGraphMetaDataRepositoryImpl(api)

    @Singleton @Provides
    fun provideApiLinkPreviewParser(): ApiLinkPreviewParser = createClient(ApiLinkPreviewParser::class.java)

    private inline fun <reified T> createClient(cl: Class<T>): T {
        val client = OkHttpClient().newBuilder()
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://google.com")
            .build()
            .create(cl)
    }

//    private fun <T> createClient(cl: Class<T>): T {
//        val client = OkHttpClient().newBuilder()
//            .readTimeout(15, TimeUnit.SECONDS)
//            .build()
//
//        return Retrofit.Builder()
//            .client(client)
//            .baseUrl("https://google.com")
//            .build()
//            .create(cl) as T
//    }
}