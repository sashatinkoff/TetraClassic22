package com.isidroid.b21.utils

/**
 * Generic class for holding success response, error response and loading status
 */
/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultData<out R> {

    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Exception) : ResultData<Nothing>()
    object Loading : ResultData<Nothing>()
    object Idle : ResultData<Nothing>()
}

/**
 * `true` if [ResultData] is of type [Success] & holds non-null [Success.data].
 */
val ResultData<*>.succeeded
    get() = this is ResultData.Success && data != null