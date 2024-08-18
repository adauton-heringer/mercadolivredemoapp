package com.adauton.mercadolivredemo.features.productdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adauton.mercadolivredemo.R
import com.adauton.mercadolivredemo.common.screens.ErrorScreen
import com.adauton.mercadolivredemo.common.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val productDetailsResult by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                },
            )
        }
    ) { paddingValues ->

        when (productDetailsResult) {
            is ProductDetailsUiState.Error -> {
                ErrorScreen()
            }

            is ProductDetailsUiState.Loading -> {
                LoadingScreen()
            }

            is ProductDetailsUiState.Success -> {

                val productDetails = (productDetailsResult as ProductDetailsUiState.Success).result

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    AsyncImage(
                        model = productDetails.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(2f),
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                    )

                    Text(text = productDetails.title, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = productDetails.price,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Description", style = MaterialTheme.typography.displaySmall)
                    Text(text = productDetails.description ?: "", style = MaterialTheme.typography.bodySmall)


                }
            }
        }
    }


}