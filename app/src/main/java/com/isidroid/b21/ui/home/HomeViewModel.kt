package com.isidroid.b21.ui.home

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.isidroid.b21.App
import com.isidroid.core.ext.copy
import com.isidroid.core.ext.copyToPublicFolder
import com.isidroid.core.ext.saveString
import com.isidroid.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun createAndSave(uri: Uri?) {
        val context = App.instance.applicationContext
        viewModelScope.launch {
            val tmp = withContext(Dispatchers.IO) {
                val file = File.createTempFile("tmp", "txt")
                file.saveString(UUID.randomUUID().toString())

                file.copyToPublicFolder(
                    context = context,
                    targetDisplayName = file.name,
                    destUri = uri!!
                )
            }
        }
    }

    fun pickImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val documentFile = DocumentFile.fromSingleUri(context, uri)
            val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")

            documentFile?.copy(context, file)

            Timber.i("sdfsdfdsf document=${documentFile?.uri}")
            Timber.i("sdfsdfdsf file=${file.absolutePath}, size=${file.length()}")
        }
    }
}