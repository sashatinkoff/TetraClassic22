package com.isidroid.b21.ui.home

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import timber.log.Timber
import java.util.concurrent.TimeUnit

/*
* We are making separate webSocketClient where all code related to socket is written and
* singleton instance of webSocketClient is made so that we can do stuffs with single socket
* from anywhere in the project
* */
class WebSocketClient2 {
    private lateinit var webSocket: okhttp3.WebSocket
    private var socketListener: SocketListener? = null
    private var socketUrl = ""
    private var shouldReconnect = true
    private var client: OkHttpClient? = null

    companion object {
        private lateinit var instance: WebSocketClient2

        @JvmStatic
        @Synchronized
        //This function gives singleton instance of WebSocket.
        fun getInstance(): WebSocketClient2 {
            synchronized(WebSocketClient2::class) {
                if (!::instance.isInitialized) {
                    instance = WebSocketClient2()
                }
            }
            return instance
        }
    }

    fun setListener(listener: SocketListener) {
        this.socketListener = listener
    }

    fun setSocketUrl(socketUrl: String) {
        this.socketUrl = socketUrl
    }

    private fun initWebSocket() {
        Timber.i("initWebSocket() socketurl = $socketUrl")
        client = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.DAYS)
            .build()

        val request = Request.Builder()
            .url(url = socketUrl)
            .build()
        webSocket = client!!.newWebSocket(request, webSocketListener)
        //this must me done else memory leak will be caused
        client!!.dispatcher.executorService.shutdown()
    }

    fun connect() {
        Timber.i("connect()")
        shouldReconnect = false
        initWebSocket()
    }

    fun reconnect() {
        Timber.i("reconnect()")
        initWebSocket()
    }

    //send
    fun sendMessage(message: String) {
        Timber.i("sendMessage($message)")
        if (::webSocket.isInitialized) webSocket.send(message)
    }


    //We can close socket by two way:

    //1. websocket.webSocket.close(1000, "Dont need connection")
    //This attempts to initiate a graceful shutdown of this web socket.
    //Any already-enqueued messages will be transmitted before the close message is sent but
    //subsequent calls to send will return false and their messages will not be enqueued.

    //2. websocket.cancel()
    //This immediately and violently release resources held by this web socket,
    //discarding any enqueued messages.

    //Both does nothing if the web socket has already been closed or canceled.
    fun disconnect() {
        if (::webSocket.isInitialized) webSocket.close(1000, "Do not need connection anymore.")
        shouldReconnect = false
    }

    interface SocketListener {
        fun onMessage(message: String)
    }


    private val webSocketListener = object : WebSocketListener() {
        //called when connection succeeded
        //we are sending a message just after the socket is opened
        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            Timber.i("onOpen()")
        }

        //called when text message received
        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
            socketListener?.onMessage(text)
        }

        //called when binary message received
        override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            Timber.i("onClosing(), reason=$reason, code=$code, ${webSocket.queueSize()}")
        }

        override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            //called when no more messages and the connection should be released
            Timber.i("onClosed()")
            if (shouldReconnect) reconnect()
        }

        override fun onFailure(
            webSocket: okhttp3.WebSocket, t: Throwable, response: Response?
        ) {
            Timber.i("onFailure() $t")
            if (shouldReconnect) reconnect()
        }
    }
}