package com.isidroid.b21.ui.home

import android.net.nsd.NsdServiceInfo

interface HomeView {
    fun startDiscovery()

    fun onServiceFound(info: NsdServiceInfo)
    fun onCleared()
    fun onResolveFailed(info: NsdServiceInfo, code: Int)
    fun onSelectService(service: NsdServiceInfo)
    fun sendMessage(message: String)
}