package com.isidroid.link_preview.data.repository;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J\u0012\u0010\f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/isidroid/link_preview/data/repository/OpenGraphMetaDataRepositoryImpl;", "Lcom/isidroid/link_preview/domain/repository/OpenGraphMetaDataRepository;", "api", "Lcom/isidroid/link_preview/data/source/network/ApiLinkPreviewParser;", "(Lcom/isidroid/link_preview/data/source/network/ApiLinkPreviewParser;)V", "organizeFetchedData", "Lcom/isidroid/link_preview/domain/model/LinkSourceContent;", "doc", "Lorg/jsoup/nodes/Document;", "url", "", "parseFullContent", "parseHeaderContent", "link_preview_release"})
public final class OpenGraphMetaDataRepositoryImpl implements com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository {
    @org.jetbrains.annotations.NotNull
    private final com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser api = null;
    
    public OpenGraphMetaDataRepositoryImpl(@org.jetbrains.annotations.NotNull
    com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser api) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public com.isidroid.link_preview.domain.model.LinkSourceContent parseFullContent(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public com.isidroid.link_preview.domain.model.LinkSourceContent parseHeaderContent(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
        return null;
    }
    
    private final com.isidroid.link_preview.domain.model.LinkSourceContent organizeFetchedData(org.jsoup.nodes.Document doc, java.lang.String url) {
        return null;
    }
}