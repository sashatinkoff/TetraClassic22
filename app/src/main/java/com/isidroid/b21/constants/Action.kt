package com.isidroid.b21.constants

import androidx.annotation.StringDef


@Retention(AnnotationRetention.SOURCE)
@StringDef
annotation class Action {
    companion object {
        const val PROJECT_SETTINGS = "REQUEST_KEY_PROJECT_SETTINGS"
    }
}
