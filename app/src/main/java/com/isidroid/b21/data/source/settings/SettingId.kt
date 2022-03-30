package com.isidroid.b21.data.source.settings

import androidx.annotation.StringRes

enum class SettingId(
    val type: SettingType,
    @StringRes val title: Int? = null,
    @StringRes val description: Int? = null
) {
    // string
    THEME(type = SettingType.INT),
    ACCESS_TOKEN(type = SettingType.STRING),
    REFRESH_TOKEN(type = SettingType.STRING);

    fun update(value: Int) {
        when (this) {
            THEME -> Settings.theme = value
        }
    }
}

enum class SettingType { BOOL, STRING, INT, LONG }