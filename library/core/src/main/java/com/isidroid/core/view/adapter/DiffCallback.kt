package com.isidroid.core.view.adapter

import androidx.recyclerview.widget.DiffUtil
import timber.log.Timber
import java.util.UUID

class DiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val listComparison: ListComparison<T>,
    private val listComparisonPayload: ListComparisonPayload<T>? = null
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listComparison.areItemsTheSame(oldList, newList, oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listComparison.areContentsTheSame(oldList, newList, oldItemPosition, newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return listComparisonPayload?.getChangePayload(oldList, newList, oldItemPosition, newItemPosition)
    }

    interface ListComparison<T> {
        fun areItemsTheSame(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
        }

        fun areContentsTheSame(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
        }
    }

    interface  ListComparisonPayload<T> {
        fun getChangePayload(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Any? = null
    }
}