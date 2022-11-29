package com.isidroid.b21.data.source.remote

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import timber.log.Timber
import java.io.InputStream

private const val BUFFER_SIZE = 1024

class ProgressEmittingRequestBody(
    private val mediaType: String,
    private val inputStream: InputStream,
    private val length: Long
) : RequestBody() {
    override fun contentType(): MediaType? = mediaType.toMediaTypeOrNull()
    override fun contentLength(): Long = length

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(BUFFER_SIZE)
        var uploaded: Long = 0
        val fileSize = length

        try {
            while (true) {

                val read = inputStream.read(buffer)
                if (read == -1) break

                uploaded += read
                sink.write(buffer, 0, read)

                val progress = (((uploaded / fileSize.toDouble())) * 100).toInt()
                Timber.i("sdfsdfsdf progress=$progress")
            }

        } catch (e: Exception) {
            Timber.e(e)
        } finally {
        }
    }
}