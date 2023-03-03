package com.isidroid.b21.utils.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.ui.main.MainView
import com.isidroid.core.ui.ErrorUiHandler
import com.isidroid.core.ui.core.CoreBindActivity

abstract class BindActivity : CoreBindActivity(), ErrorUiHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(Settings.theme)
        super.onCreate(savedInstanceState)
    }

    override fun showError(t: Throwable?) {
        t ?: return
        Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
    }
}