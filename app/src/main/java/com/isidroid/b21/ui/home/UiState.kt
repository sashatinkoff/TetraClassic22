package com.isidroid.b21.ui.home

import android.net.nsd.NsdServiceInfo

sealed interface UiState {
    data class ServiceItem(val info: NsdServiceInfo) : UiState
    object Clear : UiState
    data class ResolveFailed(val info: NsdServiceInfo, val code: Int) : UiState
    data class SelectedService(val service: NsdServiceInfo) : UiState
    data class Error(val t: Throwable): UiState
}
