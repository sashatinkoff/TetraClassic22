package com.isidroid.b21.di

import android.content.Context
import com.isidroid.b21.data.repository.SessionRepositoryImpl
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideSessionRepository(): SessionRepository = SessionRepositoryImpl()
}