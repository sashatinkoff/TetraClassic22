package com.isidroid.b21.ui.home.adapter

import android.net.nsd.NsdServiceInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.isidroid.b21.databinding.ItemCustomLoadingBinding
import com.isidroid.b21.databinding.ItemSampleBinding
import com.isidroid.core.view.adapter.CoreBindAdapterV2
import com.isidroid.core.view.adapter.CoreHolderV2
import com.isidroid.core.view.adapter.CoreLoadingHolderV2

class Adapter(
    private val listener: Listener,
) : CoreBindAdapterV2<NsdServiceInfo>() {

    override fun createHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): CoreHolderV2<out ViewDataBinding, NsdServiceInfo> {
        return Holder(ItemSampleBinding.inflate(layoutInflater, parent, false), listener)
    }

    interface Listener {
        fun clickOnServiceItem(item: NsdServiceInfo)
    }
}

data class Item(val name: String)