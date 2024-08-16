package com.adauton.mercadolivredemo.data.services

import android.util.Log
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toProductDetails
import com.adauton.mercadolivredemo.data.network.toSearchResult
import com.adauton.mercadolivredemo.models.ProductDetails
import com.adauton.mercadolivredemo.models.SearchResult
import retrofit2.HttpException
import javax.inject.Inject

class SearchServiceImpl @Inject constructor(private val searchApi: SearchApi) : SearchService {

    override suspend fun search(query: String): SearchResult {

        val result = searchApi.searchProducts(query)
        if (result.isSuccessful) {
            Log.d("DEBUGGIN", result.body().toString())
            return result.body()!!.toSearchResult()
        } else {
            throw HttpException(result)
        }

    }

    override suspend fun getProductDetails(id: String): ProductDetails =
        searchApi.getProduct(id).toProductDetails()


}