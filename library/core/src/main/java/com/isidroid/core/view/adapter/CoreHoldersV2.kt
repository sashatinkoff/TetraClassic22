package com.isidroid.core.view.adapter

import androidx.databinding.ViewDataBinding

open class CoreEmptyHolderV2<B : ViewDataBinding, T>(binding: B) : CoreHolderV2<B, T>(binding) {
    override fun bind(item: T) {}
}

open class CoreLoadingHolderV2<B : ViewDataBinding, T>(binding: B) : CoreHolderV2<B, T>(binding) {
    override fun bind(item: T) {}
    open fun onLoading() {}
}

class CoreNoInternetHolderV2<B : ViewDataBinding, T>(binding: B) : CoreHolderV2<B, T>(binding) {
    override fun bind(item: T) {}
}