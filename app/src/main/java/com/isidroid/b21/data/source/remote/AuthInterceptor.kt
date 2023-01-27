package com.isidroid.b21.data.source.remote

import com.isidroid.b21.BuildConfig
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.domain.repository.SessionRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val HEADER_AUTH = "Authorization"

class AuthInterceptor(
    private val sessionRepository: SessionRepository? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = Settings.accessToken

        val request = requestBuilder(
            originalRequest = chain.request(),
            accessToken = token
        ).build()


        val response = chain.proceed(request)

        return when (response.code) {
            401 -> refreshToken(chain, request, response)
            else -> response
        }
    }

    private fun refreshToken(
        chain: Interceptor.Chain,
        request: Request?,
        response: Response
    ): Response {
        if (sessionRepository == null || request == null) return response

        response.close()
        sessionRepository.refreshToken()

        val builder = request.newBuilder().removeHeader(HEADER_AUTH)
        authorizeCall(builder, Settings.accessToken)

        return chain.proceed(builder.build())
    }

    private fun requestBuilder(originalRequest: Request, accessToken: String?): Request.Builder {
        val cookie = ""

        val builder = originalRequest.newBuilder()
            .addHeader("App-Version", BuildConfig.VERSION_NAME)
            .addHeader("Cookie", cookie)

//            .addHeader("User-Agent", "AndroidOS")

//        builder.addHeader("Accept", "application/json")
        val skipAuth = originalRequest.headers.any { it.first == "skip_auth" }

        if (!skipAuth) authorizeCall(builder, accessToken)
        return builder
    }

    private fun authorizeCall(builder: Request.Builder, accessToken: String?) {
//        accessToken?.let { builder.addHeader(HEADER_AUTH, "Bearer $it") }
    }
}