package com.isidroid.b21.data.source.local

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.isidroid.b21.data.source.remote.DateDeserializer
import java.util.*

private const val STRING_DELIMITER = "::@::"

class RoomConverters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateDeserializer())
        .setLenient()
        .create()


    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}