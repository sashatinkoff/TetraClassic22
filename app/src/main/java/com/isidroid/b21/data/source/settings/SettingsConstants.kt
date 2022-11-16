package com.isidroid.b21.data.source.settings

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(SettingType.THEME, SettingType.ACCESS_TOKEN, SettingType.REFRESH_TOKEN, SettingType.APP_LAUNCH)
annotation class SettingType {
    companion object {
        const val THEME = "THEME"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val APP_LAUNCH = "APP_LAUNCH"
    }
}