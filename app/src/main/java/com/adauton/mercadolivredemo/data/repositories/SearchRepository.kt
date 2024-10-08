package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.flow.StateFlow

interface SearchRepository {
    val productsResult: StateFlow<Result<SearchResult>>
    suspend fun searchProducts(query: String, offset: Int = 0)
}