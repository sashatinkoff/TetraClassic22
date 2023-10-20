package com.isidroid.link_preview.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\u0002H\u0004\"\u0006\b\u0000\u0010\u0004\u0018\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0006H\u0082\b\u00a2\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0007\u00a8\u0006\r"}, d2 = {"Lcom/isidroid/link_preview/di/LinkPreviewModule;", "", "()V", "createClient", "T", "cl", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "provideApiLinkPreviewParser", "Lcom/isidroid/link_preview/data/source/network/ApiLinkPreviewParser;", "provideOpenGraphMetaData", "Lcom/isidroid/link_preview/domain/repository/OpenGraphMetaDataRepository;", "api", "link_preview_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class LinkPreviewModule {
    @org.jetbrains.annotations.NotNull
    public static final com.isidroid.link_preview.di.LinkPreviewModule INSTANCE = null;
    
    private LinkPreviewModule() {
        super();
    }
    
    @javax.inject.Singleton
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository provideOpenGraphMetaData(@org.jetbrains.annotations.NotNull
    com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser api) {
        return null;
    }
    
    @javax.inject.Singleton
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser provideApiLinkPreviewParser() {
        return null;
    }
}