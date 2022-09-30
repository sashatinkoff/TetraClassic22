package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.isidroid.b21.App
import com.isidroid.b21.domain.usecase.TelegramPdfUseCase
import com.isidroid.core.ext.*
import com.isidroid.core.utils.BaseViewModel
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val useCase: TelegramPdfUseCase
) : BaseViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun createAndSave(uri: Uri?) {
        val context = App.instance.applicationContext
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val file = File.createTempFile("tmp", "txt")
                file.saveString(UUID.randomUUID().toString())

                file.copyToPublicFolder(
                    context = context,
                    targetDisplayName = file.name,
                    destUri = uri!!
                )


                "Hello world ${UUID.randomUUID()}".saveContentToFile(
                    context = context,
                    targetDisplayName = "helloworld.txt",
                    destUri = uri!!
                )
            }
        }
    }

    fun read(uri: Uri?) {
        uri ?: return
        val context = App.instance.applicationContext

        viewModelScope.launch(Dispatchers.IO) {
            val result = DocumentFile.fromTreeUri(context, uri)
            result!!.listFiles().forEach { file ->
                if (file.name!!.endsWith(".txt")) {
                    val content = file.readText(context)
                    Timber.i("${file.name}=$content")
                } else {
                    val file2 = File.createTempFile("sdfsdf", "${file.name}")
                    val inputStream = context.contentResolver.openInputStream(file.uri)
                    file2.outputStream().use { inputStream?.copyTo(it) }

                    Timber.i("${file2.absolutePath}, length=${file2.length()}/${file.length()}")
                }
            }
        }
    }

    fun createPdf(uri: Uri) {
        viewModelScope.launch {

            useCase.create(uri)
                .flowOn(Dispatchers.IO)
                .collect {
                    Timber.i("$it")
                }

        }
    }
}

