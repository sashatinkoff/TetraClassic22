package com.isidroid.core.ext

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

val Fragment.parent get() = (parentFragment as? NavHostFragment)?.parentFragment ?: parentFragment