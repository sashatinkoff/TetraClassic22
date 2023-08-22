package com.isidroid.b21.ui.home

import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import com.isidroid.core.ext.catchTimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState = _viewState.asStateFlow()

    private var _nsdService: NsdServiceInfo? = null

    fun add(service: NsdServiceInfo) {
        viewModelScope.launch {
            flowOf(service)
                .flowOn(Dispatchers.Main)
                .onEach { _viewState.emit(UiState.ServiceItem(it)) }
                .collect()
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val service = _nsdService ?: run {
                _viewState.emit(UiState.Error(Exception("Service not selected")))
                return@launch
            }

            useCase.sendMessage(service, message)
                .flowOn(Dispatchers.IO)
                .catchTimber { _viewState.emit(UiState.Error(it)) }
                .collect {

                }
        }
    }

    fun onDiscoveryStarted() {
        viewModelScope.launch { _viewState.emit(UiState.Clear) }
    }

    fun resolveFailed(info: NsdServiceInfo?, code: Int) {
        info ?: return
        viewModelScope.launch { _viewState.emit(UiState.ResolveFailed(info, code)) }
    }

    fun selectService(service: NsdServiceInfo) {
        this._nsdService = service
        viewModelScope.launch { _viewState.emit(UiState.SelectedService(service)) }
    }

    sealed interface UiState {
        data class ServiceItem(val info: NsdServiceInfo) : UiState
        object Clear : UiState
        data class ResolveFailed(val info: NsdServiceInfo, val code: Int) : UiState
        data class SelectedService(val service: NsdServiceInfo) : UiState
        data class Error(val t: Throwable) : UiState
    }
}