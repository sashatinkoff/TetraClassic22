package com.isidroid.b21.domain.use_case

import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HomeUseCase @Inject constructor(
) {

    fun sendMessage(item: NsdServiceInfo, message: String) = flow<Unit> {
        Timber.i("create socket to ${item.host}:${item.port}")
        val socket = Socket(item.host, item.port)

        Timber.i("socket.create isConnected=${socket.isConnected}")

        val out = PrintWriter(
            BufferedWriter(
                OutputStreamWriter(socket.getOutputStream())
            ), true
        )
        out.println(message)
        out.flush()

        socket.close()


        Timber.i("socket.close isConnected=${socket.isConnected}")

    }
}