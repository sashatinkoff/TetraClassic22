package com.isidroid.b21.data.source.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date?> {

    override fun deserialize(
        jsonElement: JsonElement, typeOF: Type,
        context: JsonDeserializationContext
    ): Date? {
        val formats = arrayOf(
            "yyyy-MM-dd",
            "MMM dd, yyyy HH:mm:ss",
            "MMM dd, yyyy",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ssZZZZ"
        )

        for (format in formats) {
            try {
                return SimpleDateFormat(format, Locale.getDefault()).parse(jsonElement.asString)
            } catch (e: ParseException) {
//                Timber.e(e)
            }

        }

        return null
    }

}