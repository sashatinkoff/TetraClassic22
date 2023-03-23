package com.isidroid.core.view.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class CoreHolderV2<B : ViewDataBinding, T>(protected val binding: B) : RecyclerView.ViewHolder(binding.root) {
    fun update(item: T) {
        bind(item)
        onBind()
    }

    fun update(item: T, payloads: Any) {
        bind(item, payloads)
        onBind()
    }

    abstract fun bind(item: T)
    open fun bind(item: T, payloads: Any) {}
    open fun onViewRecycled() {}

    private fun onBind() {
        if (binding.hasPendingBindings())
            binding.executePendingBindings()
    }
}
