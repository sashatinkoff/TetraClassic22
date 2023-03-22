package com.isidroid.b21.ui.home.adapter

import java.util.*

data class Item(val id: Int, var name: String, var createdAt: Date) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}