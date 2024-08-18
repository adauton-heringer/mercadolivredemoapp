package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.models.ProductDetails
import kotlinx.coroutines.flow.StateFlow

interface ProductDetailsRepository {
    val productDetails: StateFlow<Result<ProductDetails>>
    suspend fun getProductDetails(id: String)
}