package com.adauton.mercadolivredemo.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.SearchResultDto
import com.adauton.mercadolivredemo.data.network.toSearchResult
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import com.adauton.mercadolivredemo.data.repositories.SearchRepositoryImpl
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class SearchRepositoryTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchApi: SearchApi
    private lateinit var testDispatcher: TestDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        searchApi = mock()
        searchRepository = SearchRepositoryImpl(searchApi, testDispatcher)
    }

    @Test
    fun `Given a success api call, when searchProducts is called, then productsResult is Success`() =
        runTest(testDispatcher) {
            // Given
            val searchResultDtoTest = SearchResultDto("", listOf())
            val searchResultTest = searchResultDtoTest.toSearchResult()
            whenever(searchApi.searchProducts(any(), any())).thenReturn(
                Response.success(
                    searchResultDtoTest
                )
            )
            val results = mutableListOf<Result<SearchResult>>()
            val job = launch {
                searchRepository.productsResult.toList(results)
            }
            // When
            searchRepository.searchProducts("")
            job.cancel()

            // Then
            assertEquals(Result.Loading, results[0])
            assertEquals(Result.Success(searchResultTest), results[1])
        }

    @Test
    fun `Given an error api call, when searchProducts is called, then productsResult is Error`() =
        runTest(testDispatcher) {
            // Given
            val errorResponse = Response.error<SearchResultDto>(400, byteArrayOf().toResponseBody())
            whenever(searchApi.searchProducts(any(), any())).thenReturn(
                errorResponse
            )

            val results = mutableListOf<Result<SearchResult>>()
            val job = launch {
                searchRepository.productsResult.toList(results)
            }
            // When
            searchRepository.searchProducts("")
            job.cancel()

            // Then
            assertEquals(Result.Loading, results[0])
            assertEquals(errorResponse.message(), (results[1] as Result.Error).exception.message)
        }
}