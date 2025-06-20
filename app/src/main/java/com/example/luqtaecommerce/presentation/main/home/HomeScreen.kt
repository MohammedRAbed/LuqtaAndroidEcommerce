package com.example.luqtaecommerce.presentation.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.main.categories.CategoryItem
import com.example.luqtaecommerce.presentation.main.products.ProductItem
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.ShimmerCategoriesRow
import com.example.luqtaecommerce.ui.components.ShimmerProductItem
import com.example.luqtaecommerce.ui.components.ShimmerProductsGrid
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {

    LaunchedEffect(viewModel) {
        delay(500)
        viewModel.initialFetchPreviewCategories()
        delay(1000)
        viewModel.initialFetchLatestProducts()
    }


    val categoriesState = viewModel.categories.collectAsState().value
    val latestProductsState = viewModel.latestProducts.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        HomeAppBar()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Welcome Header
            WelcomeHeader(navController)

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
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Categories.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (categoriesState) {
                is Result.Success -> {
                    val categoryList = categoriesState.data
                    CategoriesRow(categoryList) { categorySlug, categoryName ->
                        navController.navigate(
                            "${Screen.Products.route}/${categorySlug}/${categoryName}"
                        )
                    }
                }

                is Result.Loading -> {
                    ShimmerCategoriesRow()
                    /*Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryCyan)
                    }*/
                }

                is Result.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            onClick = { viewModel.fetchPreviewCategories() }
                        ) {
                            Text("إعادة التحميل")
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Retry"
                            )
                        }
                    }

                    Log.e(
                        "PreviewCategories Error",
                        categoriesState.message
                            ?: categoriesState.exception.localizedMessage
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
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Products.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (latestProductsState) {
                is Result.Success -> {
                    val productsGrid = latestProductsState.data.chunked(2)
                    ProductsGrid(
                        productChunks = productsGrid
                    ) { productSlug ->
                        navController.navigate(
                            "${Screen.ProductDetails.route}/${productSlug}"
                        )
                    }
                }

                is Result.Loading -> {
                    repeat(2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            repeat(2) {
                                Box(modifier = Modifier.weight(1f)) {
                                    ShimmerProductItem()
                                }
                            }
                        }
                    }
                    /*
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryCyan)
                    }
                     */
                }

                is Result.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            onClick = { viewModel.fetchLatestProducts() }
                        ) {
                            Text("إعادة التحميل")
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Retry"
                            )
                        }
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
fun HomeAppBar(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
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

    Spacer(modifier = Modifier.height(12.dp))

    HorizontalDivider(
        color = LightPrimary,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 6.dp)
    )
}

@Composable
private fun WelcomeHeader(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8DDDCF)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "سلام محمد! ",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "يلا ندوّر على ",
                        fontSize = 18.sp,
                    )
                    Text(
                        text = "لقطة",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "؟",
                        fontSize = 18.sp,
                    )
                }
                Button(
                    onClick = { navController.navigate(Screen.Categories.route) },
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        text = "ابدأ التسوق",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                    androidx.compose.material3.Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = "Retry"
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.waving),
                modifier = Modifier.size(70.dp),
                contentDescription = "Hi"
            )
        }


    }
}

@Composable
private fun CategoriesRow(
    categories: List<Category>,
    onCategoryClick: (String, String) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach {
            CategoryItem(
                category = it,
                modifier = Modifier.weight(1f)
            ) {
                onCategoryClick(it.slug, it.name)
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
                ) {
                    onProductClick(product.slug)
                }
            }

            // Fill empty space if only 1 item in last row
            if (rowItems.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}