package com.isidroid.b21.ui.home.adapter

import java.util.*

data class Item(val id: Int = kotlin.random.Random.nextInt(), var name: String, var createdAt: Date = Date()) {
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

    override fun toString(): String {
        return "$id"
    }


}