package com.isidroid.b21.utils.views.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.b21.R

abstract class CoreBindAdapter<T> : RecyclerView.Adapter<CoreHolder>() {
    private var loadMoreCallback: (() -> Unit)? = null
    protected var hasMore = false
    protected open var hasEmpty = false
    protected open val loadingResource: Int = R.layout.item_loading
    protected open var emptyResource: Int = R.layout.item_empty
    protected open val hasInitialLoading = false
    protected var isInserted = false
    protected var currentPosition = -1
    protected var noInternetConnection = false

    var items = mutableListOf<T>()

    val isNoNetworkState
        get() = noInternetConnection && items.isEmpty()

    fun onLoadMore(callback: (() -> Unit)) = apply { this.loadMoreCallback = callback }

    open fun onDestroy() = apply {}
    open fun onPause() = apply {}
    open fun onResume() = apply {
        if (currentPosition >= 0) notifyItemChanged(currentPosition, currentPosition)
    }

    @CallSuper
    fun add(vararg items: T) = apply {
        insert(items.asList())
        notifyDataSetChanged()
    }

    @CallSuper
    fun remove(vararg items: T) = apply {
        items.forEach { this.items.remove(it) }
        notifyDataSetChanged()
    }

    fun create() = apply {
        hasMore = hasInitialLoading
        onCreate()
    }

    fun more(more: Boolean = true) = apply {
        this.hasMore = more
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (noInternetConnection && items.isEmpty()) 1
        else if (items.isEmpty() && !hasMore && hasEmpty && isInserted) 1
        else {
            var size = items.size
            if (hasMore) size++
            size
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

    private fun getResource(viewType: Int): Int {
        return when (viewType) {
            VIEW_TYPE_NO_INTERNET -> R.layout.item_no_internet_connection
            VIEW_TYPE_LOADING -> loadingResource
            VIEW_TYPE_EMPTY -> emptyResource
            else -> resource(viewType)
        }
    }

    fun <T : ViewDataBinding> bindType(parent: ViewGroup, viewType: Int): T {
        val inflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate(inflater, getResource(viewType), parent, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> createLoadingHolder(bindType(parent, viewType))
            VIEW_TYPE_EMPTY -> createEmptyHolder(bindType(parent, viewType))
            VIEW_TYPE_NO_INTERNET -> createEmptyHolder(bindType(parent, viewType))
            else -> createHolder(bindType(parent, viewType), viewType)
        }
    }

    final override fun onBindViewHolder(holder: CoreHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_LOADING -> updateLoadingViewHolder(holder as CoreLoadingHolder, position)
            VIEW_TYPE_EMPTY -> {
            }
            else -> updateViewHolder(holder, position)
        }

        (holder as? CoreBindHolder<*, *>)?.let { onBindHolder(it.binding, position) }
    }

    private fun updateLoadingViewHolder(holder: CoreLoadingHolder, position: Int) {
        holder.bind(position)
        loadMore()
    }

    private fun updateViewHolder(holder: CoreHolder, position: Int) {
        try {
            val item = items[position]
            onUpdateHolder(holder, item)
            (holder as? CoreBindHolder<T, ViewDataBinding>)?.bind(item)
        } catch (e: Exception) {
        }
    }

    fun update(item: T, isAnimate: Boolean = false) {
        findPosition(item) {
            items[it] = item
            onUpdate(item)
            notifyItemChanged(it, if (isAnimate) null else it)
        }
    }

    fun remove(item: T) {
        findPosition(item) {
            items.remove(item)
            onRemove(item)
            notifyItemRemoved(it)
        }
    }

    private fun findPosition(item: T, callback: (pos: Int) -> Unit) {
        val position = items.indexOf(item)
        if (position >= 0) callback.invoke(position)
    }

    @CallSuper
    open fun insert(items: List<T>, hasMore: Boolean = false) {
        this.noInternetConnection = false
        this.hasMore = hasMore
        this.isInserted = true

        items.intersect(this.items).forEach {
            val position = this.items.indexOf(it)
            this.items[position] = it
        }

        val all = mutableListOf<T>()
        all.addAll(this.items)
        all.addAll(items)

        this.items = all.distinct().toMutableList()
        notifyDataSetChanged()
    }

    fun loadMore() {
        synchronized(this) {
            Handler(Looper.getMainLooper()).postDelayed({ loadMoreCallback?.invoke() }, 500)
        }
    }

    @CallSuper
    open fun clear() = apply {
        isInserted = false
        items.clear()
        notifyDataSetChanged()
    }

    fun reset() = apply {
        isInserted = false
        hasMore = hasInitialLoading
        noInternetConnection = false
        items.clear()

        onReset()
        notifyDataSetChanged()
    }

    @CallSuper
    override fun onViewRecycled(holder: CoreHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    // Open and abstract functions
    abstract fun resource(viewType: Int): Int

    open fun createLoadingHolder(binding: ViewDataBinding): CoreHolder = CoreLoadingHolder(binding)
    open fun createEmptyHolder(binding: ViewDataBinding): CoreHolder = CoreEmptyHolder(binding)
    open fun createHolder(binding: ViewDataBinding, viewType: Int): CoreHolder = Holder<T>(binding)

    open fun onBindHolder(binding: ViewDataBinding, position: Int) {}
    open fun onUpdateHolder(holder: CoreHolder, item: T) {}

    open fun onCreate() {}
    open fun onReset() {}

    open fun onUpdate(item: T) {}
    open fun onRemove(item: T) {}
    open fun onActive(position: Int) {
        if (position == currentPosition || position < 0) return
        val oldPosition = currentPosition
        currentPosition = position

        notifyItemChanged(position, position)
        notifyItemChanged(oldPosition, oldPosition)
    }

    open fun noNetworkConnectionError() {
        if (items.isEmpty()) {
            noInternetConnection = true
            notifyDataSetChanged()
        } else {
            insert(listOf(), false)
        }
    }


    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_EMPTY = 2
        const val VIEW_TYPE_NO_INTERNET = 900
    }

    open class Holder<T>(b: ViewDataBinding) : CoreBindHolder<T, ViewDataBinding>(b) {
        override fun onBind(item: T) {}
    }

    open class CoreLoadingHolder(b: ViewDataBinding) : CoreBindHolder<Int, ViewDataBinding>(b) {
        override fun onBind(item: Int) {}
    }

    open class CoreEmptyHolder(b: ViewDataBinding) : CoreBindHolder<String, ViewDataBinding>(b) {
        override fun onBind(item: String) {}
    }
}

abstract class CoreHolder(v: View) : RecyclerView.ViewHolder(v) {
    open fun onViewRecycled() {}
}