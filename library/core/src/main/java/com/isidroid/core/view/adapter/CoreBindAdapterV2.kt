package com.isidroid.core.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.core.databinding.ItemEmptyBinding
import com.isidroid.core.databinding.ItemLoadingBinding

abstract class CoreBindAdapterV2<T>(
    var hasMore: Boolean = false,
    private var hasEmpty: Boolean = false,
    private var loadingListener: LoadingListener? = null
) : RecyclerView.Adapter<CoreHolderV2<out ViewDataBinding, T>>(), CoreBindAdapterCallback<T> {
    private var items = mutableListOf<T>()
    private var noInternetConnection = false
    private var isInserted = false

    val list: List<T> get() = items

    // ==== Adapter core ====
    override fun getItemCount(): Int {
        return when {
            noInternetConnection && items.isEmpty() -> 1
            items.isEmpty() && !hasMore && hasEmpty && isInserted -> 1
            else -> {
                var size = items.size
                if (hasMore) size++
                size
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && noInternetConnection -> VIEW_TYPE_NO_INTERNET
            position == itemCount - 1 && hasMore -> VIEW_TYPE_LOADING
            position == 0 && items.size == 0 && hasEmpty && isInserted -> VIEW_TYPE_EMPTY
            else -> VIEW_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreHolderV2<out ViewDataBinding, T> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_LOADING -> createLoadingHolder(layoutInflater, parent)
            VIEW_TYPE_EMPTY -> createEmptyHolder(layoutInflater, parent)
            VIEW_TYPE_NO_INTERNET -> createNoInternetHolder(layoutInflater, parent)
            else -> createHolder(parent, layoutInflater, viewType)
        }
    }

    override fun onBindViewHolder(holder: CoreHolderV2<out ViewDataBinding, T>, position: Int) {
        (holder as? CoreLoadingHolderV2)?.also {
            it.onLoading()
            loadingListener?.loadMore()
        }
        getItem(position)?.also { holder.update(it) }
    }

    override fun onBindViewHolder(holder: CoreHolderV2<out ViewDataBinding, T>, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (payloads.isNotEmpty())
            getItem(position)?.also { holder.update(it, payloads) }
    }

    override fun onViewRecycled(holder: CoreHolderV2<out ViewDataBinding, T>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    // ==== Creation ====
    override fun createLoadingHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreLoadingHolderV2<out ViewDataBinding, T> =
        CoreLoadingHolderV2(ItemLoadingBinding.inflate(layoutInflater, parent, false))

    override fun createEmptyHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreEmptyHolderV2<out ViewDataBinding, T> =
        CoreEmptyHolderV2(ItemEmptyBinding.inflate(layoutInflater, parent, false))

    override fun createNoInternetHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreNoInternetHolderV2<out ViewDataBinding, T> =
        CoreNoInternetHolderV2(ItemEmptyBinding.inflate(layoutInflater, parent, false))

    // ==== DiffCallback.ListComparisonPayload ====
    override fun getChangePayload(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Any? {
        return null
    }

    // ==== DiffCallback.ListItemComparison ====
    override fun areContentsTheSame(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.getOrNull(oldItemPosition) == newList.getOrNull(newItemPosition)
    }

    // ==== Data manipulation ====
    override fun getItem(position: Int) = items.getOrNull(position)

    override fun clear() = apply {
        val count = items.size

        items.clear()
        notifyItemRangeRemoved(0, count)
        hasMore = false
    }

    override fun reset(hasEmpty: Boolean) {
        this.noInternetConnection = false
        this.hasEmpty = hasEmpty
        this.isInserted = false
    }

    override fun insert(list: List<T>, hasMore: Boolean, listComparisonPayload: DiffCallback.ListComparisonPayload<T>?) {
        val newList = mutableListOf<T>()
        newList.addAll(items)
        newList.addAll(list)

        insert(oldList = this.items, newList = newList, hasMore = hasMore, listComparisonPayload = listComparisonPayload, listContentsComparison = this)
    }

    override fun remove(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun remove(vararg items: T) {
        val oldList = mutableListOf<T>()
        oldList.addAll(this.items)

        for (item in items)
            this.items.remove(item)

        if (oldList.size != this.items.size) {
            // let's pass updates to list

            val callback = DiffCallback(
                oldList = oldList,
                newList = this.items,
                listContentsComparison = this,
                listItemComparison = this,
                listComparisonPayload = null,
            )
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun update(items: List<T>, listContentsComparison: DiffCallback.ListContentsComparison<T>, listComparisonPayload: DiffCallback.ListComparisonPayload<T>?) {
        val oldList = this.items.toMutableList()
        val newList = this.items.toMutableList()

        for (item in items) {
            val index = newList.indexOf(item)
            if (index >= 0)
                newList[index] = item
            else
                newList.add(item)
        }

        insert(
            oldList = oldList,
            newList = newList,
            hasMore = this.hasMore,
            listComparisonPayload = listComparisonPayload,
            listContentsComparison = listContentsComparison
        )
    }

    override fun update(item: T, payload: Any?) {
        val index = items.indexOf(item)
        if (index >= 0) {
            items[index] = item
            if (payload != null)
                notifyItemChanged(index, payload)
            else
                notifyItemChanged(index)
        }
    }

    override fun add(list: List<T>, startPosition: Int?) {
        val newList = items.toMutableList()

        if (startPosition != null)
            newList.addAll(startPosition, list)
        else
            newList.addAll(list)

        insert(oldList = this.items, newList = newList, hasMore = this.hasMore, listComparisonPayload = null, listContentsComparison = this)
    }

    override fun add(item: T, position: Int?) {
        val newList = items.toMutableList()

        if (position != null && position in 0..items.size)
            newList.add(position, item)
        else
            newList.add(item)

        insert(oldList = this.items, newList = newList, hasMore = this.hasMore, listComparisonPayload = null, listContentsComparison = this)
    }

    private fun insert(
        oldList: List<T>,
        newList: List<T>,
        hasMore: Boolean,
        listComparisonPayload: DiffCallback.ListComparisonPayload<T>?,
        listContentsComparison: DiffCallback.ListContentsComparison<T>
    ) {
        if (this.hasMore) {
            this.hasMore = false
            notifyItemRemoved(itemCount + 1)
        }

        if (hasMore)
            notifyItemInserted(itemCount)

        val callback = DiffCallback(
            oldList = oldList,
            newList = newList,
            listContentsComparison = listContentsComparison,
            listComparisonPayload = listComparisonPayload,
            listItemComparison = this,
        )

        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)

        this.isInserted = true
        this.hasMore = hasMore
        this.items = newList.toMutableList()
    }

    // ==== utils ====
    override fun noInternetConnection(noConnected: Boolean) {
        this.noInternetConnection = noConnected
    }

    fun interface LoadingListener {
        fun loadMore()
    }

    companion object {
        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_EMPTY = 2
        const val VIEW_TYPE_NO_INTERNET = 3
        const val VIEW_TYPE_NORMAL = 4
    }
}