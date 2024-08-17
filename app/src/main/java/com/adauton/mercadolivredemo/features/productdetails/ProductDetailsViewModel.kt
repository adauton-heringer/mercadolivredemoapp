package com.adauton.mercadolivredemo.features.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import com.adauton.mercadolivredemo.models.ProductDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _productDetails = MutableStateFlow(ProductDetails(title = "", id = ""))
    val productDetails = _productDetails.asStateFlow()

    init {
        viewModelScope.launch {
            searchRepository.productDetails.collect { productDetails ->
                _productDetails.update { productDetails }
            }
        }
    }
}