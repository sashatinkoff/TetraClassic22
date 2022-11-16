package com.isidroid.b21.constants

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef()
annotation class Argument {
    companion object {
        const val NAME = "NAME"
    }
}