package com.isidroid.core.ext

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.net.UnknownHostException
import java.util.ArrayList

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

/**
 * Return the sum of Triple items or null.
 */
fun Triple<Int?, Int?, Int?>.sum(): Int? {
    if (toList().any { it == null })
        return null


    return (first ?: 0) + (second ?: 0) + (third ?: 0)
}

/**
 * The default toString method returns "null" if the value is null. For better readability
 * we want to show an empty string.
 */
fun Int?.toStringOrEmpty(): String {
    return this?.toString() ?: ""
}

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

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, clazz)
    else
        getParcelable<T>(key)
}

inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializable(key, clazz)
    else
        getSerializable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.getParcelableListCompat(key: String, clazz: Class<T>): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelableArrayList(key, clazz)
    else
        getParcelableArrayList<T>(key)
}

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializableExtra(key, clazz)
    else
        getSerializableExtra(key) as? T
}


inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelableExtra(key, clazz)
    else
        getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Intent.getParcelableArrayListExtraCompat(key: String, clazz: Class<T>): List<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelableArrayListExtra(key, clazz)
    else
        getParcelableArrayListExtra<T>(key)
}