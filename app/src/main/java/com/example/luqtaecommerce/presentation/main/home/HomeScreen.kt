package com.example.luqtaecommerce.presentation.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.main.categories.CategoryItem
import com.example.luqtaecommerce.presentation.main.products.ProductItem
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getPreviewCategories()
        viewModel.getLatestProducts()
    }

    val categoriesState = viewModel.categories.collectAsState().value
    val latestProductsState = viewModel.latestProducts.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "لُقطة",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(5.dp))
            Image(
                painter = painterResource(id = R.drawable.app_temp_logo2),
                modifier = Modifier.size(32.dp),
                contentDescription = "App Icon" // Provide a meaningful content description
            )
        }

        HorizontalDivider(
            color = LightPrimary,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Welcome Header
            WelcomeHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Categories Section
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "التصنيفات",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "عرض الكل",
                    color = PrimaryCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (categoriesState) {
                is Result.Success -> {
                    val categoryList = categoriesState.data
                    CategoriesRow(categoryList) {}
                }

                is Result.Loading -> {
                    Box(modifier = Modifier.size(40.dp)) {
                        CircularProgressIndicator(color = PrimaryCyan)
                    }
                }

                is Result.Error -> {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = { viewModel.getPreviewCategories() }
                    ) {
                        Text("إعادة المحاولة")
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Retry"
                        )
                    }

                    Log.e(
                        "PreviewCategories Error",
                        categoriesState.message ?: categoriesState.exception.localizedMessage
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Latest Products Section
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "أحدث المنتجات",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "عرض الكل",
                    color = PrimaryCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (latestProductsState) {
                is Result.Success -> {
                    val productsGrid = latestProductsState.data.chunked(2)
                    ProductsGrid(productsGrid) {}
                }

                is Result.Loading -> {
                    Box(modifier = Modifier.size(40.dp)) {
                        CircularProgressIndicator(color = PrimaryCyan)
                    }
                }

                is Result.Error -> {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = { viewModel.getLatestProducts() }
                    ) {
                        Text("إعادة المحاولة")
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Retry"
                        )
                    }

                    Log.e(
                        "LatestProducts Error",
                        latestProductsState.message
                            ?: latestProductsState.exception.localizedMessage
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "سلام محمد!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "يلا نبلّش تسوق؟",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "👋",
                fontSize = 48.sp
            )
        }
    }
}

@Composable
private fun CategoriesRow(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach {
            CategoryItem(
                category = it,
                modifier = Modifier.weight(1f)
            ) {
                onCategoryClick(it.slug)
            }
        }
    }
}

@Composable
private fun ProductsGrid(
    productChunks: List<List<Product>>,
    onProductClick: (String) -> Unit
) {
    productChunks.forEach { rowItems ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowItems.forEach { product ->
                ProductItem(
                    modifier = Modifier.weight(1f),
                    product = product
                )
            }

            // Fill empty space if only 1 item in last row
            if (rowItems.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}