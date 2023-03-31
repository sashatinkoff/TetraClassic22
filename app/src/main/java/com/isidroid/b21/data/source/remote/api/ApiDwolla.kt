package com.isidroid.b21.data.source.remote.api

import com.isidroid.b21.data.source.remote.request.DwollaCreateCustomerReq
import com.isidroid.b21.data.source.remote.response.DwollaCustomerStatusResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiDwolla {
    @POST("customers")
    fun createCustomer(@Body request: DwollaCreateCustomerReq): Call<ResponseBody>

    @GET("customers/{customerId}")
    fun checkStatus(@Path("customerId") customerId: String): Call<DwollaCustomerStatusResponse>
}