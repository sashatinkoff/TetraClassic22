package com.isidroid.b21.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.isidroid.b21.databinding.ItemSampleBinding
import com.isidroid.b21.ui.home.adapter.Holder
import com.isidroid.b21.ui.home.adapter.Item
import com.isidroid.core.view.adapter.CoreBindAdapterV2
import com.isidroid.core.view.adapter.CoreHolderV2
import timber.log.Timber

class Adapter(private val listener: Listener) : CoreBindAdapterV2<Item>() {

    override fun createHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): CoreHolderV2<out ViewDataBinding, Item> {
        return Holder(ItemSampleBinding.inflate(layoutInflater, parent, false), listener)
    }

    interface Listener {
        fun updateTime(item: Item)
        fun updateComplete(item: Item)
        fun delete(item: Item)
    }
}