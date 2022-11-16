package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.CartCharacter

interface HomeView {
    fun onListReady(list: List<CartCharacter>, total: Int)
    fun onCharacterReady(character: CartCharacter)
}