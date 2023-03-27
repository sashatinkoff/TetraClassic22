package com.isidroid.b21.di

import android.content.Context
import com.google.gson.Gson
import com.isidroid.b21.App
import com.isidroid.b21.data.repository.LiveJournalRepositoryImpl
import com.isidroid.b21.data.repository.PdfRepositoryImpl
import com.isidroid.b21.data.repository.PostRepositoryImpl
import com.isidroid.b21.data.repository.SessionRepositoryImpl
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.remote.api.ApiLiveJournal
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.b21.domain.repository.PdfRepository
import com.isidroid.b21.domain.repository.PostRepository
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
    fun provideContext(): Context = App.instance

    @Singleton
    @Provides
    fun provideSessionRepository(): SessionRepository = SessionRepositoryImpl()

    @Singleton
    @Provides
    fun provideLiveJournalRepository(gson: Gson, appDatabase: AppDatabase, context: Context): LiveJournalRepository = LiveJournalRepositoryImpl(gson, appDatabase, context)

    @Singleton
    @Provides
    fun providePdf( appDatabase: AppDatabase, apiLiveJournal: ApiLiveJournal): PdfRepository = PdfRepositoryImpl(appDatabase, apiLiveJournal)

    @Singleton @Provides
    fun providePostRepository(appDatabase: AppDatabase): PostRepository = PostRepositoryImpl(appDatabase.postDao)
}