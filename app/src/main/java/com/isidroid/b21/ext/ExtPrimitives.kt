package com.isidroid.b21.ext

import java.util.concurrent.TimeUnit

val Boolean?.bool
    get() = this ?: false

val Long.nanosToSec
    get() = TimeUnit.NANOSECONDS.toSeconds(this)