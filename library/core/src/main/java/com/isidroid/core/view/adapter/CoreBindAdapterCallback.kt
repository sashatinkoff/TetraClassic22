package com.isidroid.core.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

interface CoreBindAdapterCallback<T> : DiffCallback.ListContentsComparison<T>, DiffCallback.ListComparisonPayload<T>, DiffCallback.ListItemComparison<T> {
    // ==== Creation ====
    fun createHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): CoreHolderV2<out ViewDataBinding, T>
    fun createLoadingHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreHolderV2<out ViewDataBinding, T>
    fun createEmptyHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreHolderV2<out ViewDataBinding, T>
    fun createNoInternetHolder(layoutInflater: LayoutInflater, parent: ViewGroup): CoreHolderV2<out ViewDataBinding, T>

    // ==== Data manipulation ====
    fun getItem(position: Int): T?
    fun add(item: T, position: Int? = null)
    fun add(list: List<T>, startPosition: Int? = null)
    fun update(item: T, payload: Any? = null)
    fun update(
        items: List<T>,
        listContentsComparison: DiffCallback.ListContentsComparison<T> = this,
        listComparisonPayload: DiffCallback.ListComparisonPayload<T>? = null,
    )

    fun remove(position: Int)
    fun remove(vararg items: T)
    fun insert(list: List<T>, hasMore: Boolean = false, listComparisonPayload: DiffCallback.ListComparisonPayload<T>? = null)
    fun clear(): CoreBindAdapterV2<T>
    fun reset(hasEmpty: Boolean)


    // ==== utils ====
    fun noInternetConnection(noConnected: Boolean = true)
}