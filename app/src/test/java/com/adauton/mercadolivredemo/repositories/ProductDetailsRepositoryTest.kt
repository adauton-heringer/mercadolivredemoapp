package com.adauton.mercadolivredemo.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.network.ProductDescriptionDto
import com.adauton.mercadolivredemo.data.network.ProductDetailsDto
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toProductDetails
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepository
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepositoryImpl
import com.adauton.mercadolivredemo.models.ProductDetails
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

class ProductDetailsRepositoryTest {

    private lateinit var productDetailsRepository: ProductDetailsRepository
    private lateinit var searchApi: SearchApi
    private lateinit var testDispatcher: TestDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        searchApi = mock()
        testDispatcher = UnconfinedTestDispatcher()
        productDetailsRepository = ProductDetailsRepositoryImpl(searchApi, testDispatcher)
    }

    @Test
    fun `Given a success api call, when getProductDetails is called, then productDetails is Success`() =
        runTest(testDispatcher) {
            // Given
            val productDetailsDtoTest = ProductDetailsDto(
                title = "",
                productId = "",
                price = 0.0,
                imageUrl = "",
            )
            val productDetailsTest = productDetailsDtoTest.toProductDetails(null)
            whenever(searchApi.getProductDetails(any())).thenReturn(
                Response.success(
                    productDetailsDtoTest
                )
            )
            whenever(searchApi.getProductDescription(any())).thenReturn(
                Response.success(
                    ProductDescriptionDto(null)
                )
            )
            val results = mutableListOf<Result<ProductDetails>>()
            val job = launch {
                productDetailsRepository.productDetails.toList(results)
            }
            // When
            productDetailsRepository.getProductDetails("")
            job.cancel()

            // Then
            assertEquals(Result.Loading, results[0])
            assertEquals(Result.Success(productDetailsTest), results[1])
        }

    @Test
    fun `Given an error api call, when searchProducts is called, then productsResult is Error`() =
        runTest(testDispatcher) {
            // Given
            val errorResponse = Response.error<ProductDetailsDto>(400, byteArrayOf().toResponseBody())
            whenever(searchApi.getProductDetails(any())).thenReturn(
                errorResponse
            )
            whenever(searchApi.getProductDescription(any())).thenReturn(
                Response.success(ProductDescriptionDto(null))
            )

            val results = mutableListOf<Result<ProductDetails>>()
            val job = launch {
                productDetailsRepository.productDetails.toList(results)
            }
            // When
            productDetailsRepository.getProductDetails("")
            job.cancel()

            // Then
            assertEquals(Result.Loading, results[0])
            assertEquals(Result.Error(Throwable("Error fetching product details")).toString(), results[1].toString())
        }
}