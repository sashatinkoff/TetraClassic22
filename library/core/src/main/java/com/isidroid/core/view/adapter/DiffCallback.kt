package com.isidroid.core.view.adapter

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val listContentsComparison: ListContentsComparison<T>,
    private val listItemComparison: ListItemComparison<T>,
    private val listComparisonPayload: ListComparisonPayload<T>? = null
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listItemComparison.areItemsTheSame(oldList, newList, oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listContentsComparison.areContentsTheSame(oldList, newList, oldItemPosition, newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return listComparisonPayload?.getChangePayload(oldList, newList, oldItemPosition, newItemPosition)
    }

    fun interface ListContentsComparison<T> {
        fun areContentsTheSame(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Boolean
    }

    interface ListItemComparison<T> {
        fun areItemsTheSame(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
        }
    }

    fun interface ListComparisonPayload<T> {
        fun getChangePayload(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Any?
    }
}