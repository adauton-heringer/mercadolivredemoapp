package com.adauton.mercadolivredemo.data.network

import com.adauton.mercadolivredemo.models.Product
import com.adauton.mercadolivredemo.models.SearchResult
import com.google.gson.annotations.SerializedName

data class SearchResultDto(
    @SerializedName("query")
    val query: String,
    @SerializedName("results")
    val products: List<ProductDto>,
)

data class ProductDto(
    @SerializedName("title")
    val title: String,
)

fun SearchResultDto.toSearchResult(): SearchResult =
    SearchResult(
        query = query,
        products = products.map { it.toProduct() }
    )

fun ProductDto.toProduct(): Product =
    Product(
        title = title,
    )