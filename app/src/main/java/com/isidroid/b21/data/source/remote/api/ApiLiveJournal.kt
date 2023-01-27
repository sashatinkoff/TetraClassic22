package com.isidroid.b21.data.source.remote.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiLiveJournal {
    @GET
    fun get(@Url url: String): Call<ResponseBody>
}