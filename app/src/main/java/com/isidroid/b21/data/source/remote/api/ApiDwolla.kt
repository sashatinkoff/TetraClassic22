package com.isidroid.b21.data.source.remote.api

import com.isidroid.b21.data.source.remote.request.DwollaCreateCustomerReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiDwolla {
    @POST("customers")
    @Headers("Content-Type: application/vnd.dwolla.v1.hal+json; Accept: application/vnd.dwolla.v1.hal+json; Authorization: Bearer pBA9fVDBEyYZCEsLf/wKehyh1RTpzjUj5KzIRfDi0wKTii7DqY")
    fun createCustomer(@Body request: DwollaCreateCustomerReq): Call<ResponseBody>
}