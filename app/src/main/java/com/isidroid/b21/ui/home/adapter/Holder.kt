package com.isidroid.b21.ui.home.adapter

import com.isidroid.b21.databinding.ItemSampleBinding
import com.isidroid.b21.ui.home.Adapter
import com.isidroid.core.ext.stringDateTime
import com.isidroid.core.ext.stringDateTimeSec
import com.isidroid.core.view.adapter.CoreHolderV2

class Holder(b: ItemSampleBinding, private val listener: Adapter.Listener) : CoreHolderV2<ItemSampleBinding, Item>(b) {

    override fun bind(item: Item) {
        with(binding) {
            headerView.text = "${item.id}"
            headerUpdateView.text = "complete "
            dateView.text = item.createdAt.stringDateTimeSec
            textView.text = item.name

            buttonUpdateTime.setOnClickListener { listener.updateTime(item) }
            buttonUpdateComplete.setOnClickListener { listener.updateComplete(item) }
            buttonDelete.setOnClickListener { listener.delete(item) }
        }
    }

    override fun bind(item: Item, payloads: Any) {
        with(binding) {
            headerUpdateView.text = "partially"
            textView.text = "payloads=$payloads"
            dateView.text = item.createdAt.stringDateTimeSec
        }
    }
}