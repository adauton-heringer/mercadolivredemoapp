package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.*
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toProductDetails
import com.adauton.mercadolivredemo.data.network.toSearchResult
import com.adauton.mercadolivredemo.data.services.SearchService
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
//    private val searchService: SearchService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SearchRepository {

    private val _products = MutableStateFlow<Result<SearchResult>>(Result.Loading)
    override val productsResult = _products.asStateFlow()

    private val _productDetails = MutableStateFlow(ProductDetails("", ""))
    override val productDetails = _productDetails.asStateFlow()

    override suspend fun searchProducts(query: String, offset: Int) {
        withContext(Dispatchers.IO) {
            _products.update { Result.Loading }
            try {
                val response = searchApi.searchProducts(query, offset)
                if (response.isSuccessful) {
                    response.body()?.let { searchResultDto ->
                        _products.update { Result.Success(searchResultDto.toSearchResult()) }
                    }
                } else {
                    _products.update { Result.Error(Throwable(response.message())) }
                }
            } catch (e: IOException) {
                _products.update { Result.Error(e) }
            }
        }
    }

    override suspend fun getProductDetails(id: String): ProductDetails {
        return withContext(Dispatchers.IO) {
            val productDetails = searchApi.getProduct(id).toProductDetails()
            _productDetails.update { productDetails }
            productDetails
        }
    }
}