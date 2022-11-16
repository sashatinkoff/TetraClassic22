package com.isidroid.b21.data.repository

import android.os.RemoteException
import com.isidroid.b21.data.mapper.CharacterMapper
import com.isidroid.b21.data.source.remote.api.ApiRickMorty
import com.isidroid.b21.domain.model.CartCharacter
import com.isidroid.b21.domain.model.ListResult
import com.isidroid.b21.domain.repository.RickMortyRepository
import com.isidroid.b21.ext.result

class RickMortyRepositoryImpl(private val api: ApiRickMorty) : RickMortyRepository {

    override suspend fun characters(): ListResult<CartCharacter> {
        val response = api.characters().execute()
        val result = response.result { RemoteException("List") }

        return ListResult(
            total = result.info.count,
            list = result.result.map { CharacterMapper.transform(it) }
        )
    }

    override suspend fun loadCharacter(id: Int): CartCharacter {
        val response = api.loadCharacter(id).execute()
        val result = response.result { RemoteException("Character $id not found") }

        return CharacterMapper.transform(result)
    }
}