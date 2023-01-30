package com.isidroid.b21.domain.repository

import android.content.Context
import android.net.Uri
import java.util.Date

interface PdfRepository {
    suspend fun create(context: Context, uri: Uri)
    suspend fun create(context: Context, uri: Uri, start: Date, end: Date, pdfFileName: String)
}