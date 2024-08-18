package com.adauton.mercadolivredemo.viewmodels

import com.adauton.mercadolivredemo.MainDispatcherRule
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import com.adauton.mercadolivredemo.features.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchViewModel: SearchViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        searchRepository = mock()
        searchViewModel = SearchViewModel(searchRepository)
    }

    @Test
    fun `when user types a new query to search, update query stream`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val newQuery = "test"
            val results = mutableListOf<String>()
            val job = launch {
                searchViewModel.query.toList(results)
            }
            // When
            searchViewModel.onQueryChange(newQuery)
            job.cancel()

            // Then
            assertEquals("", results[0])
            assertEquals(newQuery, results[1])
        }

    @Test
    fun `Given search is not active, when isActive is called, then isActive is true`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val results = mutableListOf<Boolean>()
            val job = launch {
                searchViewModel.isActive.toList(results)
            }
            // When
            searchViewModel.isActive(true)
            job.cancel()

            // Then
            assertEquals(false, results[0])
            assertEquals(true, results[1])
        }

    @Test
    fun `Given showCloseButton is true and query is empty, when clear is called, then closeButton is false`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val results = mutableListOf<Boolean>()
            val job = launch {
                searchViewModel.isActive.toList(results)
            }
            // When

            searchViewModel.isActive(true)
            searchViewModel.clearQuery()
            job.cancel()

            // Then
            assertEquals(false, results[0])
            assertEquals(true, results[1])
            assertEquals(false, results[2])
        }

    @Test
    fun `When searchProducts is called, then searchRepository is called`() = runTest {
        // Given
        whenever(searchRepository.searchProducts(any(), any())).thenReturn(Unit)

        // When
        searchViewModel.searchProducts("")

        // Then
        verify(searchRepository).searchProducts(any(), any())
    }
}