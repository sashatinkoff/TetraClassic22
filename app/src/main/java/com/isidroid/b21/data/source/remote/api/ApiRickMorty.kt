package com.isidroid.b21.data.source.remote.api

import com.isidroid.b21.data.source.remote.response.ApiResponse
import com.isidroid.b21.data.source.remote.response.CharacterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiRickMorty {
    @GET("character")
    fun characters(): Call<ApiResponse<List<CharacterResponse>>>

    @GET("character/{id}")
    fun loadCharacter(@Path("id") id: Int): Call<CharacterResponse>

    @POST("upload")
    @Multipart
    fun uploadVideoToServer(
        @Part body: MultipartBody.Part,
    ): Call<ResponseBody>
}