package com.adauton.mercadolivredemo.data.network

import com.adauton.mercadolivredemo.models.ProductDetails
import com.google.gson.annotations.SerializedName

data class ProductDetailsDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("id")
    val productId: String,
)

fun ProductDetailsDto.toProductDetails(): ProductDetails =
    ProductDetails(
        title = title,
        id = productId,
    )
