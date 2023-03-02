package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isidroid.b21.domain.usecase.State
import com.isidroid.b21.domain.usecase.TelegramPdfUseCase
import com.isidroid.core.ext.catchTimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: TelegramPdfUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState = _viewState.asStateFlow()

    fun start() {
        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                val file = File.createTempFile("tmp", "txt")
//                file.saveString(UUID.randomUUID().toString())
//
//                file.copyToPublicFolder(
//                    context = context,
//                    targetDisplayName = file.name,
//                    destUri = uri!!
//                )
//
//
//                "Hello world ${UUID.randomUUID()}".saveContentToFile(
//                    context = context,
//                    targetDisplayName = "helloworld.txt",
//                    destUri = uri!!
//                )
//            }
//        }
        }
    }

    fun read(uri: Uri?) {
//        uri ?: return
//        val context = App.instance.applicationContext
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = DocumentFile.fromTreeUri(context, uri)
//            result!!.listFiles().forEach { file ->
//                if (file.name!!.endsWith(".txt")) {
//                    val content = file.readText(context)
//                    Timber.i("${file.name}=$content")
//                } else {
//                    val file2 = File.createTempFile("sdfsdf", "${file.name}")
//                    val inputStream = context.contentResolver.openInputStream(file.uri)
//                    file2.outputStream().use { inputStream?.copyTo(it) }
//
//                    Timber.i("${file2.absolutePath}, length=${file2.length()}/${file.length()}")
//                }
//            }
//        }
    }

    fun createPdf(uri: Uri, name: String) {
        viewModelScope.launch {
            useCase.create(uri, name)
                .flowOn(Dispatchers.IO)
                .catchTimber { _viewState.value = UiState.Error(it) }
                .collect {
                    _viewState.value = when (it) {
                        State.OnComplete -> UiState.Complete
                        is State.OnProgress -> UiState.Progress(it.current, it.max)
                    }
                }

        }
    }
}
