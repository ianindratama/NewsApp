package com.ianindratama.newsapp.core.domain.news

import app.cash.turbine.test
import com.ianindratama.newsapp.core.domain.news.repository.INewsRepository
import com.ianindratama.newsapp.core.domain.news.usecase.NewsInteractor
import com.ianindratama.newsapp.core.testutils.news
import com.ianindratama.newsapp.core.utils.Resource
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class NewsInteractorTest {

    private lateinit var repo: INewsRepository
    private lateinit var interactor: NewsInteractor

    @Before
    fun setUp() {
        repo = mockk()
        interactor = NewsInteractor(repo)
    }

    @Test
    fun `getAllHighlightedNews - emits loading then success`() = runTest {
        val flow = flowOf(
            Resource.Loading(),
            Resource.Success(listOf(news(1), news(2)))
        )
        every { repo.getAllHighlightedNews() } returns flow

        interactor.getAllHighlightedNews().test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals(listOf(news(1), news(2)).map { it.id }, success.data?.map { it.id })
            awaitComplete()
        }

        verify(exactly = 1) { repo.getAllHighlightedNews() }
    }

    @Test
    fun `getAllSearchedNews - passes query and relays emissions`() = runTest {
        val searchQuery = "android"
        every { repo.getAllSearchedNews(searchQuery) } returns flowOf(
            Resource.Loading(),
            Resource.Success(listOf(news(10)))
        )

        interactor.getAllSearchedNews(searchQuery).test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals(10L, success.data?.single()?.id)
            awaitComplete()
        }

        verify(exactly = 1) { repo.getAllSearchedNews(searchQuery) }
    }

    @Test
    fun `getAllFavoriteNews - relays multi emissions`() = runTest {
        every { repo.getAllFavoriteNews() } returns flow {
            emit(listOf(news(1, isFavorite = true)))
            emit(listOf(news(1, isFavorite = true), news(2, isFavorite = true)))
        }

        interactor.getAllFavoriteNews().test {
            assertEquals(listOf(1L), awaitItem().map { it.id })
            assertEquals(listOf(1L, 2L), awaitItem().map { it.id })
            awaitComplete()
        }

        verify { repo.getAllFavoriteNews() }
    }

    @Test fun `getNews - emits current and updated entity`() = runTest {
        val id = 42L
        every { repo.getNews(id) } returns flow {
            emit(news(id, isFavorite = false))
            emit(news(id, isFavorite = true))
        }

        interactor.getNews(id).test {
            assertFalse(awaitItem().isFavorite)
            assertTrue(awaitItem().isFavorite)
            awaitComplete()
        }

        verify { repo.getNews(id) }
    }

    @Test fun `setFavoriteNews - delegates with exact params`() {
        every { repo.setFavoriteNews(5L, true) } just Runs

        interactor.setFavoriteNews(5L, true)

        verify(exactly = 1) { repo.setFavoriteNews(5L, true) }
    }

    @After
    fun tearDown() {
        confirmVerified(repo)
        clearAllMocks()
    }
}