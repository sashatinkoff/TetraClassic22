package com.isidroid.b21.ext

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.net.UnknownHostException

val ViewDataBinding.context: Context
    get() = root.context

val ViewDataBinding.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun ViewDataBinding.getString(@StringRes res: Int) = context.getString(res)

val Throwable?.isNetworkConnectionError
    get() = this is UnknownHostException

val Any?.json: String
    get() = Gson().toJson(this)

inline fun <reified T> Gson.fromJson(json: String?): T = fromJson(json, object : TypeToken<T>() {}.type)
inline fun <reified T> Gson.fromJson(obj: JsonObject): T? = fromJson(obj, object : TypeToken<T>() {}.type)
inline fun <reified T> Any.isType(): Boolean = this is T

val Bundle?.debug: String?
    get() {
        this ?: return null
        return keySet().joinToString(", ") { "$it=${get(it)}" }
    }

inline fun <T> tryCatch(block: () -> T): T? = try {
    block()
} catch (t: Throwable) {
    null
}