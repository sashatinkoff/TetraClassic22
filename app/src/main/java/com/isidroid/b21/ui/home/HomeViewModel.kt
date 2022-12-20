package com.isidroid.b21.ui.home

import android.widget.Toast
import androidx.lifecycle.*
import com.isidroid.b21.App
import com.isidroid.b21.utils.WebSocketClient
import com.isidroid.core.ext.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

const val SOCKET_URL = "wss://staging.getweho.app/socket.io/?EIO=4&transport=websocket"
const val TEST_SOCKET_URL = "wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableStateFlow<State>(State.Empty)
    val viewState: StateFlow<State> get() = _viewState

    @Volatile
    private var webSocketClient: WebSocketClient? = null

    fun startListen() = callbackFlow {
        val webSocketClient = WebSocketClient(object : WebSocketClient.Listener {
            override fun onMessage(text: String) {
                trySend(text)
            }

            override fun onOpen() {
                trySend("onOpen")
            }

            override fun onClosing() {
                trySend("onClosing")
            }

            override fun onClosed() {
                trySend("onClosed")
            }

            override fun onFailure(t: Throwable) {
                trySend("onFailure ${t.message}")
            }
        })

        this@HomeViewModel.webSocketClient = webSocketClient

        webSocketClient.connect(TEST_SOCKET_URL)

        awaitClose {
            Timber.e("sfsdsf AWAIT_CLOSE")
            webSocketClient.disconnect()
            this@HomeViewModel.webSocketClient = null
        }
    }

    fun stopListen() {
        webSocketClient?.disconnect()
    }
}
