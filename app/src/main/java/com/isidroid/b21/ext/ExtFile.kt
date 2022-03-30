package com.isidroid.b21.ext

import java.io.File
import java.io.InputStream

fun File.copyInputStreamToFile(inputStream: InputStream?) {
    outputStream().use { fileOut -> inputStream?.copyTo(fileOut) }
}