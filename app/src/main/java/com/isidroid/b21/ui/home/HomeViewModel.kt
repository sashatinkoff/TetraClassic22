package com.isidroid.b21.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.isidroid.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel() {

    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState
}