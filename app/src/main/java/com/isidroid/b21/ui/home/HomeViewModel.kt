package com.isidroid.b21.ui.home

import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import com.isidroid.b21.ui.home.adapter.Item
import com.isidroid.core.ext.catchTimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState = _viewState.asStateFlow()

    var page = 0

    fun makePreview(link: Array<String>) {
        viewModelScope.launch {
            useCase.preview(link)
                .flowOn(Dispatchers.IO)
                .catchTimber { }
                .collect()
        }
    }

    fun loadNext() {
        viewModelScope.launch {
            flow {
                delay(3000)

                val items = (0..4).map { Item(id = (page * 10) + it, name = UUID.randomUUID().toString()) }
                emit(items)
            }
                .flowOn(Dispatchers.Default)
                .collect {
                    _viewState.value = UiState.Data(it, hasMore = page < 4)
                    page++
                }
        }
    }
}