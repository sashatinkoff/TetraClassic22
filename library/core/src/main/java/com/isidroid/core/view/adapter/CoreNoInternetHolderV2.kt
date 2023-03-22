package com.isidroid.core.view.adapter

import androidx.databinding.ViewDataBinding

class CoreNoInternetHolderV2<B : ViewDataBinding, T>(binding: B) : CoreHolderV2<B, T>(binding) {
    override fun bind(item: T) {}
}