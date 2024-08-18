package com.adauton.mercadolivredemo.models

data class SearchResult(
    val query: String,
    val products: List<Product>,
)

data class Product(
    val id: String,
    val title: String?,
    val imageUrl: String?,
    val price: String,
)

data class ProductDetails(
    val title: String,
    val id: String,
    val price: String,
    val imageUrl: String,
    val description: String?,
)