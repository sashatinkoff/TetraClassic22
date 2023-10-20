package com.isidroid.link_preview.data.source.network;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u0007"}, d2 = {"Lcom/isidroid/link_preview/data/source/network/ApiLinkPreviewParser;", "", "get", "Lretrofit2/Call;", "Lokhttp3/ResponseBody;", "url", "", "link_preview_release"})
public abstract interface ApiLinkPreviewParser {
    
    @retrofit2.http.GET
    @retrofit2.http.Streaming
    @retrofit2.http.Headers(value = {"User-Agent: Mozilla/5.0; Accept-Encoding:gzip"})
    @org.jetbrains.annotations.NotNull
    public abstract retrofit2.Call<okhttp3.ResponseBody> get(@retrofit2.http.Url
    @org.jetbrains.annotations.NotNull
    java.lang.String url);
}