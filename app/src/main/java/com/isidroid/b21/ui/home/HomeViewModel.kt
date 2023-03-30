package com.isidroid.b21.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isidroid.b21.domain.use_case.HomeUseCase
import com.isidroid.core.ext.catchTimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState = _viewState.asStateFlow()

    fun createCustomer(name: String = "Johhny", lastName: String = UUID.randomUUID().toString().take(5)) {
        viewModelScope.launch {
            useCase.createCustomer(name, lastName)
                .flowOn(Dispatchers.IO)
                .catchTimber { }
                .collect()
        }
    }
}