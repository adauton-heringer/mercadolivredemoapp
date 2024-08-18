package com.adauton.mercadolivredemo.viewmodels

import com.adauton.mercadolivredemo.MainDispatcherRule
import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepository
import com.adauton.mercadolivredemo.features.productdetails.ProductDetailsUiState
import com.adauton.mercadolivredemo.features.productdetails.ProductDetailsViewModel
import com.adauton.mercadolivredemo.models.ProductDetails
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

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private lateinit var productDetailsRepository: ProductDetailsRepository
    private lateinit var productDetailsViewModel: ProductDetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        productDetailsRepository = FakeProductDetailsRepository()
        productDetailsViewModel = ProductDetailsViewModel(productDetailsRepository)
    }

    @Test
    fun `Given productDetailsViewModel is instantiated, when repository emits new productDetails, then ProductDetailsUiState is Success`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val results = mutableListOf<ProductDetailsUiState>()
            val job = launch { productDetailsViewModel.uiState.toList(results) }

            // When
            productDetailsViewModel = ProductDetailsViewModel(productDetailsRepository)
            productDetailsRepository.getProductDetails("")

            job.cancel()

            // Then
            assertEquals(ProductDetailsUiState.Loading, results[0])
            assertEquals(
                ProductDetailsUiState.Success(FakeProductDetailsRepository.productDetailsTest),
                results[1]
            )
        }
}

class FakeProductDetailsRepository : ProductDetailsRepository {
    private val flow = MutableStateFlow<Result<ProductDetails>>(Result.Loading)
    override val productDetails: StateFlow<Result<ProductDetails>>
        get() = flow

    override suspend fun getProductDetails(id: String) {
        flow.emit(Result.Success(productDetailsTest))
    }

    companion object {
        val productDetailsTest = ProductDetails(
            title = "",
            id = "",
            price = "",
            imageUrl = "",
            description = null
        )
    }

}
