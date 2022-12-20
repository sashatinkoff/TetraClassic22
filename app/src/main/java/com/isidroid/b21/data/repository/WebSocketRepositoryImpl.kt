package com.isidroid.b21.data.repository

import com.isidroid.b21.domain.repository.WebSocketRepository
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import timber.log.Timber

class WebSocketRepositoryImpl : WebSocketRepository {
    private val okHttpClient: OkHttpClient
    

    init {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("WebSocket").i(message) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val socketListener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
        }
    }

    fun connect(url: String) {
        val request = Request.Builder().url(url = url).build()

        val webSocket = okHttpClient.newWebSocket(request, socketListener)
        //this must me done else memory leak will be caused
        okHttpClient.dispatcher.executorService.shutdown()


    }
}