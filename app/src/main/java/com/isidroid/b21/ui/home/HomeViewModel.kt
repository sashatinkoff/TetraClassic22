package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.*
import com.isidroid.b21.App
import com.isidroid.core.ext.copyToPublicFolder
import com.isidroid.core.ext.saveString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState


}