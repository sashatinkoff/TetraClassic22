package com.isidroid.b21

import android.app.Application

class MemeTestApp : Application() {

    var url = "http://127.0.0.1:8080"

    fun getBaseUrl(): String {
        return url
    }
}