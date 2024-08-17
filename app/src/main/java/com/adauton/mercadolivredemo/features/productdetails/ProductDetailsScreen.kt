package com.adauton.mercadolivredemo.features.productdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.adauton.mercadolivredemo.features.products.ProductsViewModel

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val productDetails by viewModel.productDetails.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = productDetails.title)
        Text(text = productDetails.id)
    }
}