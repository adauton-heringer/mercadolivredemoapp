package com.adauton.mercadolivredemo.data.repositories

import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.network.SearchApi
import com.adauton.mercadolivredemo.data.network.toProductDetails
import com.adauton.mercadolivredemo.models.ProductDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class ProductDetailsRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductDetailsRepository {

    private val _productDetails = MutableStateFlow<Result<ProductDetails>>(Result.Loading)
    override val productDetails = _productDetails.asStateFlow()

    override suspend fun getProductDetails(id: String) {
        withContext(defaultDispatcher) {
            _productDetails.update { Result.Loading }
            try {
                val deferredProductDetails = async { searchApi.getProductDetails(id) }
                val deferredProductDescription = async { searchApi.getProductDescription(id) }

                val responseProductDetails = deferredProductDetails.await()
                val responseProductDescription = deferredProductDescription.await()

                if (responseProductDetails.isSuccessful && responseProductDescription.isSuccessful) {
                    val description = responseProductDescription.body()?.description
                    responseProductDetails.body()?.let { produtcDetailsDto ->
                        _productDetails.update {
                            Result.Success(
                                produtcDetailsDto.toProductDetails(
                                    description
                                )
                            )
                        }
                    }
                } else {
                    _productDetails.update { Result.Error(Throwable("Error fetching product details")) }
                }
            } catch (e: IOException) {
                _productDetails.update { Result.Error(e) }
            }
        }
    }
}
