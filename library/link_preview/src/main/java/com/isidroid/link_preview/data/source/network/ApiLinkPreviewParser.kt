package com.isidroid.link_preview.data.source.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiLinkPreviewParser {
    @GET
    @Streaming
    @Headers("User-Agent: Mozilla/5.0; Accept-Encoding:gzip")
    fun get(@Url url: String): Call<ResponseBody>
}