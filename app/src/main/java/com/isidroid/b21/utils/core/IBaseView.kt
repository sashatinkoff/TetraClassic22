package com.isidroid.b21.utils.core

import androidx.annotation.CallSuper

interface IBaseView {
    @CallSuper
    fun createBaseView() {
        createAppBar()
        createForm()
        createAdapter()
    }

    fun createAppBar(){}
    fun createForm(){}
    fun createAdapter(){}
}