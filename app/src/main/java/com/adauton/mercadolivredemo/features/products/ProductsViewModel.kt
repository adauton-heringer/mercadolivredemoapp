package com.adauton.mercadolivredemo.features.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adauton.mercadolivredemo.common.Result
import com.adauton.mercadolivredemo.data.repositories.ProductDetailsRepository
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
    private val productDetailsRepository: ProductDetailsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currProducts = listOf<Product>()

    init {
        viewModelScope.launch {
            searchRepository.productsResult.collect { productsResult ->
                (_uiState.value as? ProductsUiState.Success)?.let {
                    currProducts = it.result.products
                }
                when (productsResult) {
                    is Result.Error -> _uiState.update { ProductsUiState.Error }
                    is Result.Loading -> _uiState.update { ProductsUiState.Loading }
                    is Result.Success -> {
                        _uiState.update {
                            val newProducts =
                                (currProducts + productsResult.data.products).distinctBy { it.id }
                            ProductsUiState.Success(productsResult.data.copy(products = newProducts))
                        }
                    }
                }
            }
        }
    }

    fun getProducts(index: Int) {
        (_uiState.value as? ProductsUiState.Success)?.let { (searchResult) ->
            val listSize = searchResult.products.size
            if (index == listSize - 7) {
                viewModelScope.launch {
                    searchRepository.searchProducts(searchResult.query, listSize)
                }
            }
        }
    }

    fun getProductDetails(id: String) {
        viewModelScope.launch {
            productDetailsRepository.getProductDetails(id)
        }
    }
}

sealed interface ProductsUiState {
    data class Success(val result: SearchResult) : ProductsUiState
    data object Error : ProductsUiState
    data object Loading : ProductsUiState
}