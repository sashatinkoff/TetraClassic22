package com.isidroid.b21.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

 const val SOCKET_URL = "wss://staging.getweho.app/socket.io/?EIO=4&transport=websocket"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun listen() {
        viewModelScope.launch {
            flow {
                val socket = WebSocketClient2.getInstance()
//                socket.setSocketUrl("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self")
                socket.setSocketUrl(SOCKET_URL)
                socket.setListener(object : WebSocketClient2.SocketListener {
                    override fun onMessage(message: String) {
                        Timber.i("WebSocketClient onMessage $message on ${Thread.currentThread()}")
                    }
                })

                socket.connect()

                delay(1_000)
                socket.sendMessage("Hello world")

                emit(true)
            }
                .catch { _viewState.value = State.OnError(it) }
                .collect { }
        }
    }

}
