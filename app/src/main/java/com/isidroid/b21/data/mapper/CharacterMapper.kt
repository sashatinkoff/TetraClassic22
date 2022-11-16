package com.isidroid.b21.data.mapper

import com.isidroid.b21.data.source.remote.response.CharacterResponse
import com.isidroid.b21.domain.model.CartCharacter

object CharacterMapper {
    fun transform(response: CharacterResponse): CartCharacter {
        return CartCharacter(
            id = response.id,
            name = response.name,
            image = response.image
        )
    }
}