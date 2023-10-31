package com.isidroid.b21.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.isidroid.b21.ext.makeSingleLine
import com.isidroid.b21.ext.splitByLetter
import com.isidroid.core.ext.readText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun read(uri: Uri) = flow {
        val r = DocumentFile.fromSingleUri(context, uri)
        emit(r!!.readText(context))
    }

    fun writeHashTags(map: LinkedHashMap<String, Int>, showCounters: Boolean, isSingleLine: Boolean, isSplitByLetter: Boolean) = flow {
        val entries = map.entries

        val content = entries.joinToString("\n") { entry ->
            if (showCounters)
                "${entry.key} - ${entry.value}"
            else
                entry.key
        }

        val result = when {
            isSplitByLetter -> content.splitByLetter()
            isSingleLine -> content.makeSingleLine()
            else -> content
        }

        Timber.i("$result")
        emit(result)
    }
}