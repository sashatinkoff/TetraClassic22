package com.isidroid.b21.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.isidroid.core.ext.readText
import com.isidroid.core.ext.saveContentToFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun read(uri: Uri, ext: String) = flow {
        val result = DocumentFile.fromTreeUri(context, uri)

        val file = result?.listFiles()?.firstOrNull { it.name?.endsWith(".$ext") == true } ?: throw IllegalStateException("File not found")
        emit(file.readText(context))
    }

    fun writeHashTags(uri: Uri, map: LinkedHashMap<String, Int>, showCounters: Boolean, moreThanOne: Boolean, alreadyHaveHashTags: Boolean, saveHashTagsInFile: Boolean) = flow {
        val limit = when {
            moreThanOne && alreadyHaveHashTags -> 2
            moreThanOne || alreadyHaveHashTags -> 1
            else -> 0
        }

        val entries = map.entries.filter { it.value > limit }

        val content = entries.joinToString("\n") { entry ->
            if (showCounters)
                "${entry.key} - ${entry.value}"
            else
                entry.key
        }

        val fileName = "telegram_hash_tags.txt"
        if (saveHashTagsInFile)
            content.saveContentToFile(context, fileName, uri)

        emit(Pair(fileName.takeIf { saveHashTagsInFile }, content))
    }
}