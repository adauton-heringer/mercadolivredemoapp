package com.adauton.mercadolivredemo.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    @GET("sites/MLA/search/")
    suspend fun searchProducts(
        @Query("q") query: String,
    ): SearchResultDto

    @GET("items/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: String,
    ): ProductDetailsDto
}