package com.isidroid.b21.utils

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class WebSocketClient(private val listener: Listener) {
    private val okHttpClient = createClient()
    private var webSocket: WebSocket? = null
    private var attemptLeft = 0
    private var maxAttempts = 0

    private val shouldReconnect get() = attemptLeft > 0

    private fun createClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.i(message) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    fun connect(url: String, reconnectAttempts: Int = 0) {
        attemptLeft = reconnectAttempts
        maxAttempts = reconnectAttempts
        createSocket(url)
    }

    fun disconnect() {
        webSocket?.close(1000, "Connection closed by client")
    }

    fun sendMessage(message: String){
        webSocket?.send(message)
    }

    private fun createSocket(url: String) {
        val request = Request.Builder().url(url = url).build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Timber.i("onClosed")
            listener.onClosed()
            if (shouldReconnect)
                connect(webSocket.request().url.toString(), maxAttempts)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Timber.i("onClosing")
            listener.onClosing()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Timber.i(t)

            listener.onFailure(t)
            if (shouldReconnect)
                connect(webSocket.request().url.toString(), maxAttempts)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listener.onMessage(text)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Timber.i("onOpen")
            listener.onOpen()
        }
    }

    fun interface Listener {
        fun onOpen() {}
        fun onClosing() {}
        fun onClosed() {}
        fun onMessage(text: String)
        fun onFailure(t: Throwable) {}
    }
}