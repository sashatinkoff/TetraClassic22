package com.isidroid.b21.data.source.remote.api

import com.isidroid.b21.data.source.remote.response.ApiResponse
import com.isidroid.b21.data.source.remote.response.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRickMorty {
    @GET("character")
    fun characters(): Call<ApiResponse<List<CharacterResponse>>>

    @GET("character/{id}")
    fun loadCharacter(@Path("id") id: Int): Call<CharacterResponse>
}