package com.adauton.mercadolivredemo.data.network

import com.adauton.mercadolivredemo.models.ProductDetails
import com.google.gson.annotations.SerializedName

data class ProductDetailsDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("id")
    val productId: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("thumbnail")
    val imageUrl: String,
)


data class ProductDescriptionDto(
    @SerializedName("plain_text")
    val description: String?,
)

fun ProductDetailsDto.toProductDetails(description: String?): ProductDetails =
    ProductDetails(
        title = title,
        id = productId,
        price = "R$ %.2f".format(price).replace(".", ","),
        imageUrl = imageUrl,
        description = description,
    )
