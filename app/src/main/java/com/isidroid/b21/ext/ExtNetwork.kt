package com.isidroid.b21.ext

import android.graphics.Bitmap
import android.graphics.Matrix
import org.json.JSONObject
import retrofit2.Response
import java.io.ByteArrayOutputStream
import kotlin.math.max

fun <R> Response<R>.result(onFailed: (String) -> Throwable): R {
    val result: R? = body()

    if (!isSuccessful || result == null) {
        val errorJson = errorBody()?.string()
        val message = errorJson?.let { JSONObject(it).getString("error") } ?: "Server error"

        throw(onFailed.invoke(message))
    }

    return result
}