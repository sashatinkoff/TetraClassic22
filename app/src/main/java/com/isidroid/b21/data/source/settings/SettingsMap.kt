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

    fun string(key: SettingId, defaultValue: String?) = mapString[key.name] ?: defaultValue
    fun bool(key: SettingId, defaultValue: Boolean) = mapBool[key.name] ?: defaultValue
    fun int(key: SettingId, defaultValue: Int) = mapInt[key.name] ?: defaultValue
    fun long(key: SettingId, defaultValue: Long) = mapLong[key.name] ?: defaultValue

    fun save(key: SettingId, value: Any?) {
        sp.edit(commit = true) {
            if (value is Boolean) putBoolean(key.name, value)
            if (value is String?) putString(key.name, value)
            if (value is Int) putInt(key.name, value)
            if (value is Long) putLong(key.name, value)
        }

        recreate()
    }
}


