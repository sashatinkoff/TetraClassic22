package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow<State>(State.Empty)
    val viewState = _viewState.asStateFlow()

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val monthDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val statDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

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
                        is HomeUseCase.Result.OnPostFoundLocal -> showLogs(monthDateFormat.format(it.post.createdAt!!))
                        is HomeUseCase.Result.OnPostSaved -> showLogs("${it.post.title} ${dateFormat.format(it.post.createdAt!!)} saved")
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
                        is HomeUseCase.Result.PostSavedInPdf -> showLogs("${it.post.title} saved in ${it.fileName}")
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
        if(logs.contains(event))
            return

        viewModelScope.launch {
            useCase.stats(logs, event)
                .flowOn(Dispatchers.IO)
                .catch { _viewState.value = State.OnError(it) }
                .collect {
                    _viewState.value = State.OnStats(it.liveInternetCount, it.liveJournalCount, updatedAt = statDateFormat.format(Date()), it.liveJournalDownloaded)
                    _viewState.value = State.OnEvent(it.logs)
                }
        }
    }
}