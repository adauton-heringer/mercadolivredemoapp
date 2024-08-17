package com.adauton.mercadolivredemo.models

data class SearchResult(
    val query: String,
    val products: List<Product>,
)

data class Product(
    val id: String,
    val title: String?,
    val image: String?,
    val price: String,
)

data class ProductDetails(
    val title: String,
    val id: String,
)