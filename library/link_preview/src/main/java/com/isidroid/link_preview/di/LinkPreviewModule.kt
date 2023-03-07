package com.isidroid.link_preview.di

import com.isidroid.link_preview.data.repository.OpenGraphMetaDataRepositoryImpl
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LinkPreviewModule {
    @Singleton @Provides
    fun provideOpenGraphMetaData(): OpenGraphMetaDataRepository = OpenGraphMetaDataRepositoryImpl()
}