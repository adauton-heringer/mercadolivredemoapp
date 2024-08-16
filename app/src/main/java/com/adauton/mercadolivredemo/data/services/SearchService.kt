package com.adauton.mercadolivredemo.data.services

import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult

interface SearchService {
    suspend fun search(query: String): SearchResult
    suspend fun getProductDetails(id: String): ProductDetails
}