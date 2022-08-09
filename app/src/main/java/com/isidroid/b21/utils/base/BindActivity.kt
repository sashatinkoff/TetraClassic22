package com.isidroid.b21.utils.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.core.ui.CoreBindActivity

abstract class BindActivity : CoreBindActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(Settings.theme)
        super.onCreate(savedInstanceState)
    }
}