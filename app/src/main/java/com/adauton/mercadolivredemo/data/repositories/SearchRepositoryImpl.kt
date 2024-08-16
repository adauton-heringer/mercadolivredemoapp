package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.data.services.SearchService
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SearchRepository {

    private val _products = MutableStateFlow<SearchResult>(SearchResult("", listOf()))
    val products = _products.asStateFlow()

    override suspend fun searchProducts(query: String): SearchResult {
        return withContext(Dispatchers.IO) {
            val product = searchService.search(query)
            _products.update { product }
            product
        }
    }

    override suspend fun getProductDetails(id: String): ProductDetails {
        return withContext(Dispatchers.IO) {
            searchService.getProductDetails(id)
        }
    }
}