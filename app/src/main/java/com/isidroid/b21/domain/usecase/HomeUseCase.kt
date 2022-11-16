package com.isidroid.b21.domain.usecase

import com.isidroid.b21.domain.repository.RickMortyRepository
import com.isidroid.core.utils.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val rickMortyRepository: RickMortyRepository) {

    fun getCharacters() = flow {
        val result = rickMortyRepository.characters()
        emit(ResultData.Success(result))
    }.flowOn(Dispatchers.IO)


    fun loadCharacter(id: Int) = flow {
        val result = rickMortyRepository.loadCharacter(id)
        emit(ResultData.Success(result))
    }.flowOn(Dispatchers.IO)
}