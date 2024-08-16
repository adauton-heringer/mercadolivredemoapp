package com.adauton.mercadolivredemo.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adauton.mercadolivredemo.data.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    private val _showCloseButton = MutableStateFlow(false)
    val showCloseButton = _showCloseButton.asStateFlow()

    fun searchProducts(query: String) {
        viewModelScope.launch {
            val result = searchRepository.searchProducts(query)
        }
    }

    fun onQueryChange(query: String) {
        _query.update { query }
    }

    fun clearQuery() {
        if (_query.value.isEmpty()) {
            _isActive.update { _query.value.isNotEmpty() }
            _showCloseButton.update { false }
        } else {
            _query.update { "" }
        }
    }

    fun isActive(isActive: Boolean) {
        _isActive.update { isActive }
        _showCloseButton.update { isActive }
    }
}
