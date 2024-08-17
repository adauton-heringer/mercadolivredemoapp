package com.adauton.mercadolivredemo.features.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import com.adauton.mercadolivredemo.models.Product
import com.adauton.mercadolivredemo.models.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(listOf())
    val products = _products.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        viewModelScope.launch {
            searchRepository.productsResult.collect { productsResult ->
                when (productsResult) {
                    is Result.Error -> _uiState.update { ProductsUiState.Error }
                    is Result.Loading -> _uiState.update { ProductsUiState.Loading }
                    is Result.Success -> {
                        _uiState.update { ProductsUiState.Success(productsResult.data) }
                        _products.update { it + (productsResult.data).products }
                        _query.update { (productsResult.data).query }
                    }
                }
            }
        }
    }

    fun getProducts(index: Int) {
        if (index < products.value.size - 9) return
        viewModelScope.launch {
            searchRepository.searchProducts(query.value, products.value.size)
        }
    }

    fun getProductDetails(id: String) {
        viewModelScope.launch {
            searchRepository.getProductDetails(id)
        }
    }
}

sealed interface ProductsUiState {
    data class Success(val result: SearchResult): ProductsUiState
    data object Error : ProductsUiState
    data object Loading : ProductsUiState
}