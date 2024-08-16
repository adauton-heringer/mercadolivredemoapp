package com.adauton.mercadolivredemo.data.services

import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toProductDetails
import com.adauton.mercadolivredemo.data.network.toSearchResult
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import javax.inject.Inject

class SearchServiceImpl @Inject constructor(private val searchApi: SearchApi) : SearchService {

    override suspend fun search(query: String): SearchResult =
        searchApi.searchProducts(query).toSearchResult()

    override suspend fun getProductDetails(id: String): ProductDetails =
        searchApi.getProduct(id).toProductDetails()


}