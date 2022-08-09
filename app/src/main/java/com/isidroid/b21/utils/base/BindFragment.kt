package com.isidroid.b21.utils.base

import com.isidroid.core.ui.CoreBindFragment

abstract class BindFragment() : CoreBindFragment() {
    open val title: Int? = null
}