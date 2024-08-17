package com.adauton.mercadolivredemo.features.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adauton.mercadolivredemo.R
import com.adauton.mercadolivredemo.common.screens.ErrorScreen
import com.adauton.mercadolivredemo.common.screens.LoadingScreen
import com.adauton.mercadolivredemo.models.Product
import com.adauton.mercadolivredemo.navigation.MercadoLivreScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()
    val products by viewModel.products.collectAsState()
    val query by viewModel.query.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = query, style = MaterialTheme.typography.bodyMedium) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigate(MercadoLivreScreens.Search.name) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Navigate back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        when (uiState) {
            ProductsUiState.Error -> {
                ErrorScreen(message = "Sorry, a problem occurred. Try again later.")
            }
            ProductsUiState.Loading -> {
                LoadingScreen()
            }
            is ProductsUiState.Success -> {
                SuccessView(
                    paddingValues = paddingValues,
                    navController = navController,
                    products = products,
                    paginate = viewModel::getProducts,
                    onClick = viewModel::getProductDetails,
                )
            }
        }
    }
}

@Composable
fun SuccessView(
    paddingValues: PaddingValues,
    navController: NavController,
    products: List<Product>,
    paginate: (Int) -> Unit,
    onClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        itemsIndexed(products) { index, product ->
            paginate(index)
            ProductView(
                product = product,
                onClick = {
                    onClick(product.id)
                    navController.navigate(MercadoLivreScreens.ProductDetails.name)
                }
            )
        }
    }
}

@Composable
fun ProductView(
    product: Product,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        AsyncImage(
            model = product.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.weight(1f),
            error = painterResource(id = R.drawable.ic_launcher_foreground),

            )

        Column(
            modifier = Modifier
                .weight(3f)
                .padding(16.dp)
        ) {
            Text(
                text = product.title ?: "No title",
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.price,
                maxLines = 2,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
@Preview
fun ProductPreview() {
    MaterialTheme {
        val product = Product(
            id = "",
            title = "This is a nice title",
            image = "https://http2.mlstatic.com/D_956655-MLB71366790777_082023-O.jpg",
            price = "R$ 10.58"
        )
        ProductView(
            product = product,
            onClick = {}
        )
    }
}