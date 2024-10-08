package com.adauton.mercadolivredemo.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    @GET("sites/MLB/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("offset") offset: Int = 0,
    ): Response<SearchResultDto>

    @GET("items/{productId}")
    suspend fun getProductDetails(
        @Path("productId") productId: String,
    ): Response<ProductDetailsDto>

    @GET("items/{productId}/description")
    suspend fun getProductDescription(
        @Path("productId") productId: String,
    ): Response<ProductDescriptionDto>
}