package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.flow.StateFlow

interface SearchRepository {
    val productsResult: StateFlow<Result<SearchResult>>
    val productDetails: StateFlow<ProductDetails>
    suspend fun searchProducts(query: String, offset: Int = 0)
    suspend fun getProductDetails(id: String): ProductDetails
}