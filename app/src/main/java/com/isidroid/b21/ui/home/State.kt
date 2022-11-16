package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.CartCharacter

sealed interface State {
    data class OnError(val t: Throwable) : State
    data class OnListReady(val list: List<CartCharacter>, val total: Int) : State
    data class OnCharacterReady(val character: CartCharacter) : State
}
