package com.isidroid.b21.data.source.remote

import okhttp3.Interceptor
import okhttp3.Response

class DwollaAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "UfL2i7Xarx0UZPUEFFaL552AcxH8xLrb9eXltMv7D3rPhmwHC7"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/vnd.dwolla.v1.hal+json")
            .addHeader("Accept", "application/vnd.dwolla.v1.hal+json")
            .build()

        return chain.proceed(request)
    }
}