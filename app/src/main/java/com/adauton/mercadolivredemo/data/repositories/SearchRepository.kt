package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult

interface SearchRepository {
    suspend fun searchProducts(query: String): SearchResult
    suspend fun getProductDetails(id: String): ProductDetails
}