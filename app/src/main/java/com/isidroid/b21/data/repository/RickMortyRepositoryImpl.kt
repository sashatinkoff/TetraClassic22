package com.isidroid.b21.data.repository

import android.content.Context
import android.net.Uri
import android.os.RemoteException
import androidx.documentfile.provider.DocumentFile
import com.isidroid.b21.data.mapper.CharacterMapper
import com.isidroid.b21.data.source.remote.ProgressEmittingRequestBody
import com.isidroid.b21.data.source.remote.api.ApiRickMorty
import com.isidroid.b21.domain.model.CartCharacter
import com.isidroid.b21.domain.model.ListResult
import com.isidroid.b21.domain.repository.RickMortyRepository
import com.isidroid.b21.ext.result
import com.isidroid.core.ext.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

class RickMortyRepositoryImpl(private val api: ApiRickMorty) : RickMortyRepository {

    override suspend fun characters(): ListResult<CartCharacter> {
        val response = api.characters().execute()
        val result = response.result { RemoteException("List") }

        return ListResult(
            total = result.info.count,
            list = result.result.map { CharacterMapper.transform(it) }
        )
    }

    override suspend fun loadCharacter(id: Int): CartCharacter {
        val response = api.loadCharacter(id).execute()
        val result = response.result { RemoteException("Character $id not found") }

        return CharacterMapper.transform(result)
    }

    override suspend fun upload(context: Context, uri: Uri) {
        val documentFile = DocumentFile.fromSingleUri(context, uri) ?: return
        val inputStream = tryCatch { context.contentResolver.openInputStream(uri) } ?: return
        val length = documentFile.length()

        val videoRequestBody = ProgressEmittingRequestBody(
            mediaType = "video/mp4",
            inputStream = inputStream,
            length = length
        )

        val fileBody = MultipartBody.Part.createFormData("file", documentFile.name, videoRequestBody)
        val response = api.uploadVideoToServer(body = fileBody).execute()

        inputStream.close()

        Timber.i("sdfsdfsdf code=${response.code()}")
    }
}