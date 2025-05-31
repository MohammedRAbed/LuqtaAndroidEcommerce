package com.example.luqtaecommerce.presentation.main.products

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.Product
import org.koin.androidx.compose.koinViewModel
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.ui.components.FavouriteToggleIcon
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.components.LuqtaBackHeader
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun ProductsScreen(
    navController: NavController,
    categorySlug: String? = null,
    categoryName: String? = null,
    viewModel: ProductsViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.fetchProducts(categorySlug)
    }

    val productsState = viewModel.products.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LuqtaBackHeader(
                title = if (categorySlug != null) "$categoryName" else "المنتجات",
                navController = navController
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter"
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                }
            }
        }

        HorizontalDivider(color = LightPrimary, thickness = 1.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =
            if (productsState is Result.Loading || productsState is Result.Error)
                Arrangement.Center else Arrangement.Top
        ) {
            when (productsState) {
                is Result.Loading -> {
                    Box(modifier = Modifier.size(40.dp)) {
                        CircularProgressIndicator(color = PrimaryCyan)
                    }
                }

                is Result.Success -> {
                    if (productsState.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "لا يوجد منتجات متاحة لهذا التصنيف",)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(productsState.data) { product ->
                                ProductItem(product = product)
                            }
                        }
                    }
                }

                is Result.Error -> {
                    LoadErrorView { viewModel.fetchProducts(categorySlug) }
                    Log.e(
                        "Products Error",
                        productsState.message ?: productsState.exception.localizedMessage
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product
) {
    Card(
        modifier = modifier
            .background(Color.White)
            .border(width = 1.dp, color = LightPrimary, shape = RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // This is the key: disables ripple and other indications
                onClick = {}
            ),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = product.thumbnail
                    ?: "https://th.bing.com/th/id/R.ed0bf03e1a684437be6d46f7d420d239?rik=j59a58Mk3c9mnw&pid=ImgRaw&r=0", // Use a placeholder if no thumbnail
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.16f),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.sad_face)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    style = TextStyle(
                        lineHeight = 5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp, bottom = 4.dp),
                ) {
                    Text(
                        text = "$${product.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    FavouriteToggleIcon()
                }

            }

        }
    }
}