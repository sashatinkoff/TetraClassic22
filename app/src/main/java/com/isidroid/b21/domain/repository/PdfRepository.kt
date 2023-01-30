package com.isidroid.b21.domain.repository

import android.content.Context
import android.net.Uri
import com.isidroid.b21.domain.model.Post
import java.util.Date

interface PdfRepository {
    suspend fun create(context: Context, uri: Uri, listener: Listener)
    suspend fun create(context: Context, uri: Uri, start: Date, end: Date, pdfFileName: String, listener: Listener)

    interface Listener {
        suspend fun startPdf(fileName: String)
        suspend fun downloadImage(url: String, title: String?)
        suspend fun pdfCompleted(fileName: String)
        suspend fun onPostSavedInPdf(post: Post, fileName: String)
    }
}