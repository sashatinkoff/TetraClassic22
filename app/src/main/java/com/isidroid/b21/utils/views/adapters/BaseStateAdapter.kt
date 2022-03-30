package com.isidroid.b21.utils.views.adapters

import android.database.DataSetObserver
import android.widget.BaseAdapter

abstract class BaseStateAdapter<T> : BaseAdapter() {
    val items = mutableListOf<T>()
    override fun isEmpty() = items.isEmpty()

    override fun registerDataSetObserver(observer: DataSetObserver?) {}
    override fun getItemViewType(position: Int) = 0
    override fun getItem(position: Int) = items[position]
    override fun getViewTypeCount() = 1
    override fun isEnabled(position: Int) = true
    override fun getItemId(position: Int) = 0L
    override fun hasStableIds() = true
    override fun areAllItemsEnabled() = true
    override fun unregisterDataSetObserver(observer: DataSetObserver?) {}
    override fun getCount() = items.size

    fun clear() = apply { items.clear() }
    open fun insert(list: List<T>) {
        val i = mutableListOf<T>()
        i.addAll(items)
        i.addAll(list)

        items.clear()
        items.addAll(i.distinct())
        notifyDataSetChanged()
    }
}