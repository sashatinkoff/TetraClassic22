package com.isidroid.b21.ui.home.adapter

import android.net.nsd.NsdServiceInfo
import androidx.core.text.buildSpannedString
import com.isidroid.b21.databinding.ItemSampleBinding
import com.isidroid.core.view.adapter.CoreHolderV2

class Holder(b: ItemSampleBinding, private val listener: Adapter.Listener) : CoreHolderV2<ItemSampleBinding, NsdServiceInfo>(b) {
    override fun bind(item: NsdServiceInfo) {
        binding.textView.text = buildSpannedString {
            append("${item.serviceName}, ${item.host}")
        }

        binding.cardView.setOnClickListener { listener.clickOnServiceItem(item) }
    }
}