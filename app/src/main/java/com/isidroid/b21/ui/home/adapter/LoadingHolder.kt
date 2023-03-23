package com.isidroid.b21.ui.home.adapter

import com.isidroid.b21.databinding.ItemCustomLoadingBinding
import com.isidroid.core.view.adapter.CoreLoadingHolderV2

class LoadingHolder(b: ItemCustomLoadingBinding) : CoreLoadingHolderV2<ItemCustomLoadingBinding, Item>(b) {
    override fun onLoading() {
        with(binding) {
            textView.text = "Started loading at ${System.currentTimeMillis()}, pos=$adapterPosition"
        }
    }
}