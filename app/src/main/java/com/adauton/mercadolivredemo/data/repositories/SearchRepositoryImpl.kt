package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.*
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toSearchResult
import com.adauton.mercadolivredemo.models.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRepository {

    private val _productsResult = MutableStateFlow<Result<SearchResult>>(Result.Loading)
    override val productsResult = _productsResult.asStateFlow()


    override suspend fun searchProducts(query: String, offset: Int) {
        withContext(defaultDispatcher) {
            try {
                val response = searchApi.searchProducts(query, offset)
                if (response.isSuccessful) {
                    response.body()?.let { searchResultDto ->
                        _productsResult.update { Result.Success(searchResultDto.toSearchResult()) }
                    }
                } else {
                    _productsResult.update { Result.Error(Throwable(response.message())) }
                }
            } catch (e: IOException) {
                _productsResult.update { Result.Error(e) }
            }
        }
    }
}