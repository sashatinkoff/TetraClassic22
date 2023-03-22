package com.isidroid.core.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.core.databinding.ItemEmptyBinding
import com.isidroid.core.databinding.ItemLoadingBinding

abstract class CoreBindAdapterV2<T>(
    private var hasMore: Boolean = false,
    private var hasEmpty: Boolean = false,
    private var loadingListener: LoadingListener? = null
) : RecyclerView.Adapter<CoreHolderV2<out ViewDataBinding, T>>(), DiffCallback.ListComparison<T> {

    protected val items = mutableListOf<T>()
    private var noInternetConnection = false
    private var isInserted = false

    val list: List<T> get() = items

    override fun getItemCount(): Int {
        return if (noInternetConnection && items.isEmpty()) 1
        else if (items.isEmpty() && !hasMore && hasEmpty && isInserted) 1
        else {
            var size = items.size
            if (hasMore) size++
            size
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
        (holder as? CoreLoadingHolderV2)?.also { loadingListener?.loadMore() }
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

    protected open fun createLoadingHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreLoadingHolderV2<out ViewDataBinding, T> =
        CoreLoadingHolderV2(ItemLoadingBinding.inflate(layoutInflater, parent, false))

    protected open fun createEmptyHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreEmptyHolderV2<out ViewDataBinding, T> =
        CoreEmptyHolderV2(ItemEmptyBinding.inflate(layoutInflater, parent, false))

    protected open fun createNoInternetHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreNoInternetHolderV2<out ViewDataBinding, T> =
        CoreNoInternetHolderV2(ItemEmptyBinding.inflate(layoutInflater, parent, false))

    fun getItem(position: Int) = items.getOrNull(position)
    fun clear() = apply {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
        hasMore = false
    }

    fun reset(hasEmpty: Boolean) {
        this.noInternetConnection = false
        this.hasEmpty = hasEmpty
    }

    fun noInternetConnection(noConnected: Boolean = true) {
        this.noInternetConnection = noConnected
    }

    fun remove(item: T) {
        val newList = items.toMutableList()
        val isRemoved = newList.remove(item)
        if (isRemoved)
            insert(newList)
    }

    fun add(item: T, position: Int? = null) {
        val newList = items.toMutableList()

        if (position != null && position in 0..items.size)
            newList[position] = item
        else
            newList.add(item)

        insert(newList)
    }

    fun add(list: List<T>, startPosition: Int? = null) {
        val newList = items.toMutableList()

        if (startPosition != null)
            newList.addAll(startPosition, list)
        else
            newList.addAll(list)

        insert(newList)
    }

    fun update(item: T, payload: Any? = null) {
        val index = items.indexOf(item)
        if (index >= 0) {
            items[index] = item
            if (payload != null)
                notifyItemChanged(index, payload)
            else
                notifyItemChanged(index)
        }
    }

    fun update(list: List<T>, listComparisonPayload: DiffCallback.ListComparisonPayload<T>? = null) {
        insert(list, listComparisonPayload)
    }

    fun update(list: List<T>, hasPayloadUpdate: Boolean) {
        insert(list, listComparisonPayload = object : DiffCallback.ListComparisonPayload<T> {
            override fun getChangePayload(oldList: List<T>, newList: List<T>, oldItemPosition: Int, newItemPosition: Int): Any? {
                return if (hasPayloadUpdate) 1 else null
            }
        })
    }

    open fun insert(list: List<T>, listComparisonPayload: DiffCallback.ListComparisonPayload<T>? = null) {
        val callback = DiffCallback(oldList = items, newList = list, listComparison = this, listComparisonPayload = listComparisonPayload)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)

        this.items.clear()
        this.items.addAll(list)
    }

    abstract fun createHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): CoreHolderV2<out ViewDataBinding, T>

    fun interface LoadingListener {
        fun loadMore()
    }

    companion object {
        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_EMPTY = 2
        const val VIEW_TYPE_NO_INTERNET = 3
    }
}