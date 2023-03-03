package com.isidroid.core.ui

import android.content.res.Configuration
import androidx.annotation.AttrRes
import com.isidroid.core.R

interface ScreenUiListener {
     var topInset: Int
     val isNightMode: Boolean

     fun updateStatusBarColor(
          @AttrRes statusBarColorRes: Int = R.attr.color_transparent,
          @AttrRes navigationBarColorRes: Int = android.R.attr.colorPrimary,
          isLightStatusBarIcons: Boolean = false
     )
}