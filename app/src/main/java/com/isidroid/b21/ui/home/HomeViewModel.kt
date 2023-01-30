package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    private val logs = mutableListOf<String>()

    fun create() {
        viewModelScope.launch {
            useCase.start()
                .flowOn(Dispatchers.IO)
                .catch {
                    Timber.e(it)
                    _viewState.value = State.OnError(it)
                }
                .collect {
                    when (it) {
                        is HomeUseCase.Result.OnLoading -> {}
                        is HomeUseCase.Result.OnPostFoundLocal -> showLogs("${it.post.title} in local!")
                        is HomeUseCase.Result.OnPostSaved -> showLogs("${it.post.title} saved")
                        else -> State.Empty
                    }
                }
        }
    }

    fun stop() {
        useCase.stop()
    }

    fun createPdf(uri: Uri) {
        viewModelScope.launch {
            useCase.createPdf(uri)
                .flowOn(Dispatchers.IO)
                .catch { Timber.e(it) }
                .collect {
                    when (it) {
                        is HomeUseCase.Result.StartPdf -> showLogs("start pdf ${it.fileName}")
                        is HomeUseCase.Result.DownloadImage -> showLogs("download image ${it.url} for ${it.title}")
                        is HomeUseCase.Result.PdfCompleted -> showLogs("pdf ${it.fileName} created")
                    }
                }

        }
    }

    fun liveInternet() {
        viewModelScope.launch {
            useCase.liveInternet()
                .flowOn(Dispatchers.IO)
                .catch { Timber.e(it) }
                .collect { showLogs("LiveInternet completely saved") }
        }
    }

    private fun showLogs(event: String) {
        logs.add(0, event)

        val limited = logs.take(50)
        logs.clear()
        logs.addAll(limited)

        _viewState.value = State.OnEvent(logs)
    }
}