package com.isidroid.b21.utils.base

import com.isidroid.b21.utils.core.CoreBindFragment

abstract class BindFragment() : CoreBindFragment() {
    open val title: Int? = null
}