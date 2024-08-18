package com.adauton.mercadolivredemo.viewmodels

import com.adauton.mercadolivredemo.MainDispatcherRule
import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepository
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import com.adauton.mercadolivredemo.features.products.ProductsUiState
import com.adauton.mercadolivredemo.features.products.ProductsViewModel
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
class ProductsViewModelTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var productDetailsRepository: ProductDetailsRepository
    private lateinit var productsViewModel: ProductsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        searchRepository = FakeSearchRepository()
        productDetailsRepository = mock()
        productsViewModel = ProductsViewModel(searchRepository, productDetailsRepository)
    }

    @Test
    fun `Given productsViewModel is instantiated, when searchRepository emits new products, then ProductUiState is Success`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val results = mutableListOf<ProductsUiState>()
            val job = launch { productsViewModel.uiState.toList(results) }

            // When
            productsViewModel = ProductsViewModel(searchRepository, productDetailsRepository)
            searchRepository.searchProducts("", 0)

            job.cancel()

            // Then
            assertEquals(ProductsUiState.Loading, results[0])
            assertEquals(ProductsUiState.Success(FakeSearchRepository.searchResult), results[1])
        }

    @Test
    fun `When searchProducts is called, then searchRepository is called`() = runTest {
        // Given
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Unit)

        // When
        productsViewModel.getProductDetails("")

        // Then
        verify(productDetailsRepository).getProductDetails("")
    }
}

class FakeSearchRepository : SearchRepository {
    private val flow = MutableStateFlow<Result<SearchResult>>(Result.Loading)
    override val productsResult: StateFlow<Result<SearchResult>>
        get() = flow

    override suspend fun searchProducts(query: String, offset: Int) {
        flow.emit(Result.Success(searchResult))
    }

    companion object {
        val searchResult = SearchResult("", listOf())
    }
}

