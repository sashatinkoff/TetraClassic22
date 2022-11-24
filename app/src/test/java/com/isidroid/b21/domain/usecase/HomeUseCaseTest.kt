package com.isidroid.b21.domain.usecase

import com.isidroid.b21.domain.model.CartCharacter
import com.isidroid.b21.domain.repository.RickMortyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class HomeUseCaseTest {
    private val rickMortyRepository = mock<RickMortyRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return the same data as in repository`() = runTest {
        val testCharacter = CartCharacter(id = 1, name = "Rick", image = "https://ya.ru")

        Mockito.`when`(rickMortyRepository.loadCharacter(1)).thenReturn(testCharacter)


        val useCase = HomeUseCase(rickMortyRepository = rickMortyRepository)
        val actual = useCase.loadCharacter(1).first().data
        val expected = CartCharacter(id = 1, name = "Rick", image = "https://ya.ru")

        Assertions.assertEquals(expected, actual)

        Mockito.verify(rickMortyRepository, Mockito.only()).loadCharacter(Mockito.anyInt())
    }

    @AfterEach
    fun tearDown() {
        Mockito.reset(rickMortyRepository)
    }
}