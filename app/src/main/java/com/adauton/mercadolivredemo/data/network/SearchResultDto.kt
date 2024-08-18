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
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnail")
    val image: String,
    @SerializedName("price")
    val price: Double,
)

fun SearchResultDto.toSearchResult(): SearchResult =
    SearchResult(
        query = query,
        products = products.map { it.toProduct() }
    )

fun ProductDto.toProduct(): Product =
    Product(
        id = id,
        title = title,
        imageUrl = image,
        price = "R$ %.2f".format(price).replace(".", ","),
    )

