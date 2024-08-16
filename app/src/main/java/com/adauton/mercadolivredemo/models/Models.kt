package com.adauton.mercadolivredemo.models

data class SearchResult(
    val query: String,
    val products: List<Product>,
)

data class Product(
    val title: String?,
)

data class ProductDetails(
    val title: String,
    val id: String,
)