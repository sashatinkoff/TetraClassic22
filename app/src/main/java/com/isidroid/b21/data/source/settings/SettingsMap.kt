package com.isidroid.b21.data.source.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SettingsMap {
    lateinit var sp: SharedPreferences

    private val mapBool = hashMapOf<String, Boolean>()
    private val mapString = hashMapOf<String, String?>()
    private val mapInt = hashMapOf<String, Int>()
    private val mapLong = hashMapOf<String, Long>()

    fun init(context: Context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        recreate()
    }

    private fun recreate() {
        mapBool.clear()
        mapString.clear()
        mapInt.clear()
        mapLong.clear()

        sp.all.entries.forEach {
            if (it.value is String?) mapString[it.key] = it.value as String?
            if (it.value is Boolean) mapBool[it.key] = it.value as Boolean
            if (it.value is Int) mapInt[it.key] = it.value as Int
            if (it.value is Long) mapLong[it.key] = it.value as Long
        }
    }

    fun string(@SettingType key: String, defaultValue: String?) = mapString[key] ?: defaultValue
    fun bool(@SettingType key: String, defaultValue: Boolean) = mapBool[key] ?: defaultValue
    fun int(@SettingType key: String, defaultValue: Int) = mapInt[key] ?: defaultValue
    fun long(@SettingType key: String, defaultValue: Long) = mapLong[key] ?: defaultValue

    fun save(@SettingType key: String, value: Any?) {
        sp.edit(commit = true) {
            if (value is Boolean) putBoolean(key, value)
            if (value is String?) putString(key, value)
            if (value is Int) putInt(key, value)
            if (value is Long) putLong(key, value)
        }

        recreate()
    }
}


