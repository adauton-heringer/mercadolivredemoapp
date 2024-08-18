package com.adauton.mercadolivredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adauton.mercadolivredemo.features.productdetails.ProductDetailsScreen
import com.adauton.mercadolivredemo.features.products.ProductsScreen
import com.adauton.mercadolivredemo.features.search.SearchScreen
import com.adauton.mercadolivredemo.navigation.MercadoLivreScreens
import com.adauton.mercadolivredemo.ui.theme.MercadoLivreDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MercadoLivreDemoTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MercadoLivreScreens.Search.name
                ) {
                    composable(route = MercadoLivreScreens.Search.name) { SearchScreen(navController) }
                    composable(route = MercadoLivreScreens.Products.name) { ProductsScreen(navController) }
                    composable(route = MercadoLivreScreens.ProductDetails.name) { ProductDetailsScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MercadoLivreDemoTheme {
        Greeting("Android")
    }
}