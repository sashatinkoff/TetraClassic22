package com.isidroid.core.core

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