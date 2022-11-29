package com.isidroid.b21.domain.repository

import android.content.Context
import android.net.Uri
import com.isidroid.b21.domain.model.CartCharacter
import com.isidroid.b21.domain.model.ListResult

interface RickMortyRepository {
    suspend fun characters(): ListResult<CartCharacter>
    suspend fun loadCharacter(id: Int): CartCharacter
    suspend fun upload(context: Context, uri: Uri)
}