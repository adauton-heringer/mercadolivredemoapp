package com.adauton.mercadolivredemo.features.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepository
import com.adauton.mercadolivredemo.models.ProductDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            productDetailsRepository.productDetails.collect { productDetailsResult ->
                when (productDetailsResult) {
                    is Result.Error -> _uiState.update { ProductDetailsUiState.Error }
                    is Result.Loading -> _uiState.update { ProductDetailsUiState.Loading }
                    is Result.Success -> _uiState.update { ProductDetailsUiState.Success(productDetailsResult.data) }
                }
            }
        }
    }
}

sealed interface ProductDetailsUiState {
    data class Success(val result: ProductDetails) : ProductDetailsUiState
    data object Error : ProductDetailsUiState
    data object Loading : ProductDetailsUiState
}