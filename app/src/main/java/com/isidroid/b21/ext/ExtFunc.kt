package com.isidroid.b21.ext

import androidx.navigation.NavController
import com.isidroid.b21.R

val NavController?.hasHome get() = this?.backQueue?.any { it.destination.id == R.id.fragmentHome } == true