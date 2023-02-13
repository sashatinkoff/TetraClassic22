package com.isidroid.core.ext

import org.json.JSONObject
import retrofit2.Response

fun <R> Response<R>.result(onFailed: (String) -> Throwable): R {
    val result: R? = body()

    if (!isSuccessful || result == null) {
        val errorJson = errorBody()?.string()
        val message = errorJson?.let { JSONObject(it).getString("error") } ?: "Server error"

        throw(onFailed.invoke(message))
    }

    return result
}