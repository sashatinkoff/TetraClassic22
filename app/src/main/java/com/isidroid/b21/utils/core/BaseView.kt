package com.isidroid.b21.utils.core

import androidx.annotation.CallSuper

interface BaseView {
    @CallSuper
    fun createBaseView() {
        createAppBar()
        createForm()
        createAdapter()
    }

    fun onReady(){}
    fun createAppBar(){}
    fun createForm(){}
    fun createAdapter(){}
}