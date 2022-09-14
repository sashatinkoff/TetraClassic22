package com.isidroid.b21.ext

import java.text.SimpleDateFormat
import java.util.*

val Date.formatDateTime: String
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ru", "RU")).format(this)

val Date.formatDate: String
    get() = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU")).format(this)