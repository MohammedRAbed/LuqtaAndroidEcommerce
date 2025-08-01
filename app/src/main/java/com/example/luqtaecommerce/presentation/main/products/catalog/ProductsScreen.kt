package com.example.luqtaecommerce.presentation.main.products.catalog

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.FavouriteToggleIcon
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.components.LuqtaBackHeader
import com.example.luqtaecommerce.ui.components.ShimmerProductsGrid
import com.example.luqtaecommerce.ui.theme.CairoFontFamily
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.Purple500
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    categorySlug: String? = null,
    categoryName: String? = null,
    viewModel: ProductsViewModel// = koinNavViewModel()
) {
    val currentEntry = navController.currentBackStackEntry
    val savedStateHandle = currentEntry?.savedStateHandle

    val productsUiState = viewModel.uiState.collectAsState().value
    val listState = rememberLazyGridState()

    // --- Bottom Sheet State Management ---
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Derived state to check if we should load more
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            // Trigger load more when user is 2 items away from the end
            lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - 2 && totalItemsCount > 0
        }
    }

    LaunchedEffect(categorySlug, productsUiState.products) {
        val searchQuery = savedStateHandle?.get<String>("search_query")
        if (searchQuery.isNullOrBlank())
            viewModel.initialFetch(categorySlug)
    }

    // This LaunchedEffect listens for the result from ProductSearchScreen
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<String>("search_query")
            ?.observe(navController.currentBackStackEntry!!) { query ->
                if (query != null) {
                    viewModel.applySearch(query = query)
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            savedStateHandle?.remove<String>("search_query")
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !productsUiState.isLoading && productsUiState.pagination?.next != null) {
            Log.d("InfiniteScroll", "Attempting to load next page")
            viewModel.loadNextPage()
        }
    }


    // Conditionally display the Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            //modifier = Modifier.background(Color.White),
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            SortBottomSheetContent(
                activeSortOption = productsUiState.activeSortOption,
                onSortOptionSelected = { selectedOption ->
                    viewModel.applySort(selectedOption)
                    // Hide the sheet after selection
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            )
        }
    }

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
                title = when {
                    !productsUiState.searchQuery.isNullOrBlank() -> {
                        val len = productsUiState.searchQuery.length
                        if (len > 10)
                            "نتائج البحث:  \"${productsUiState.searchQuery.dropLast(len - 14)}\"..."
                        else
                            "نتائج البحث:  \"${productsUiState.searchQuery}\""
                    }

                    categorySlug != null -> "$categoryName"
                    else -> "المنتجات"
                },
                navController = navController
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { showBottomSheet = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = Purple500,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = { navController.navigate(Screen.ProductsSearch.route) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        tint = Purple500,
                        contentDescription = "Search"
                    )
                }
            }
        }

        HorizontalDivider(color = LightPrimary, thickness = 1.dp)

        PullToRefreshBox(
            isRefreshing = productsUiState.isRefreshing,
            onRefresh = { viewModel.refreshProducts() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (
                    (productsUiState.isLoading && productsUiState.products.isEmpty()) ||
                    productsUiState.isRefreshing
                ) { // Show initial loader
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShimmerProductsGrid()
                        //CircularProgressIndicator(color = PrimaryCyan)
                        //Text(text = "جار التحميل...")
                    }
                } else if (productsUiState.error != null) {
                    LoadErrorView { viewModel.initialFetch(categorySlug) }
                    Log.e("ProductsScreen", "Error: ${productsUiState.error}")
                } else if (productsUiState.products.isEmpty()) {
                    Text(
                        text = if (productsUiState.searchQuery.isNullOrBlank()) {
                            "لا يوجد منتجات لهذا التصنيف"
                        } else {
                            "لا يوجد نتائج للبحث"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyVerticalGrid(
                        // ...
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        items(productsUiState.products) { product ->
                            ProductItem(product = product) {
                                navController.navigate(
                                    "${Screen.ProductDetails.route}/${product.slug}"
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (productsUiState.isLoading && productsUiState.pagination?.next != null) {
                                    CircularProgressIndicator(color = Purple500)
                                } else {
                                    Text(
                                        text = "لا مزيد من المنتجات",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .background(Color.White)
            .border(width = 1.dp, color = LightPrimary, shape = RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // This is the key: disables ripple and other indications
                onClick = onClick
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
                    .background(color = GrayPlaceholder)
                    .aspectRatio(1.16f),
                contentScale = ContentScale.Crop,
                //error = painterResource(R.drawable.sad_face)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontFamily = CairoFontFamily,
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

                    FavouriteToggleIcon(size = 12.dp)
                }

            }

        }
    }
}