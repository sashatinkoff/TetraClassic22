package com.isidroid.b21.data.source.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object Settings {
    private val map = SettingsMap()

    fun init(context: Context) {
        map.init(context)
    }

    var theme: Int
        get() = map.int(SettingType.THEME, AppCompatDelegate.MODE_NIGHT_NO)
        set(value) = map.save(SettingType.THEME, value)

    var accessToken: String?
        get() = map.string(SettingType.ACCESS_TOKEN, null)
        set(value) = map.save(SettingType.ACCESS_TOKEN, value)

    var refreshToken: String?
        get() = map.string(SettingType.REFRESH_TOKEN, null)
        set(value) = map.save(SettingType.REFRESH_TOKEN, value)

    var appLaunch: Int
        get() = map.int(SettingType.APP_LAUNCH, 0)
        set(value) = map.save(SettingType.APP_LAUNCH, value)

    fun onLaunch() {
        appLaunch++
    }
}