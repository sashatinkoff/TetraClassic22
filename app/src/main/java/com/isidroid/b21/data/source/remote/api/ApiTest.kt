package com.isidroid.b21.data.source.remote.api

import com.isidroid.b21.data.source.remote.response.SendMessageResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiTest {
    @GET("/bot{botApiToken}/sendMessage")
    fun sendMessage(
        @Path("botApiToken") botApiToken: String,
        @Query("chat_id") channelId: String,
        @Query("parse_mode") parseMode: String = "HTML",
        @Query("text", encoded = false) text: String,
        @Query("disable_web_page_preview")  disableWebPagePreview: Boolean = true
    ): Call<SendMessageResponse>
}