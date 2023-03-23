package com.isidroid.b21.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.isidroid.b21.databinding.ItemCustomLoadingBinding
import com.isidroid.b21.databinding.ItemSampleBinding
import com.isidroid.b21.ui.home.adapter.Holder
import com.isidroid.b21.ui.home.adapter.Item
import com.isidroid.b21.ui.home.adapter.LoadingHolder
import com.isidroid.core.view.adapter.CoreBindAdapterV2
import com.isidroid.core.view.adapter.CoreHolderV2
import com.isidroid.core.view.adapter.CoreLoadingHolderV2
import timber.log.Timber

class Adapter(
    private val listener: Listener,
    loadingListener: LoadingListener
) : CoreBindAdapterV2<Item>(loadingListener = loadingListener) {

    override fun createHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): CoreHolderV2<out ViewDataBinding, Item> {
        return Holder(ItemSampleBinding.inflate(layoutInflater, parent, false), listener)
    }

    override fun createLoadingHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreLoadingHolderV2<out ViewDataBinding, Item> {
        return LoadingHolder(ItemCustomLoadingBinding.inflate(layoutInflater, parent, false))
    }

    interface Listener {
        fun updateTime(item: Item)
        fun updateComplete(item: Item)
        fun delete(item: Item)
    }
}