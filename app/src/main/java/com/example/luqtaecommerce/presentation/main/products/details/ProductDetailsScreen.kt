package com.example.luqtaecommerce.presentation.main.products.details

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.main.cart.CartViewModel
import com.example.luqtaecommerce.ui.components.FavouriteToggleIcon
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.theme.Purple50
import com.example.luqtaecommerce.ui.theme.Purple500
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    slug: String,
    currentUser: User?,
    productDetailsViewModel: ProductDetailsViewModel = koinViewModel(),
    cartViewModel: CartViewModel = koinViewModel()
) {
    val cartState = cartViewModel.cartState.collectAsState().value
    val showAddToCartMessage = productDetailsViewModel.showAddToCartMessage.collectAsState().value

    val reviewsState by productDetailsViewModel.productReviewsState.collectAsState()
    val addReviewState by productDetailsViewModel.addReviewState.collectAsState()
    val hasUserReviewed by productDetailsViewModel.hasUserReviewed.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productDetailsViewModel.getProductDetails(slug)
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex==1) {
            productDetailsViewModel.getReviews(slug)
        }
    }

    LaunchedEffect(showAddToCartMessage) {
        if (showAddToCartMessage)
            Toast.makeText(context, "تم إضافة المنتج إلى السلة ✅🛒", Toast.LENGTH_SHORT).show()
    }

    when (val productDetailState =
        productDetailsViewModel.productDetailsState.collectAsState().value) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Purple500)
            }
        }

        is Result.Error -> {
            LoadErrorView { productDetailsViewModel.getProductDetails(slug) }
            Log.e(
                "Product Details Error",
                productDetailState.message ?: productDetailState.exception.localizedMessage
            )
        }

        is Result.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                /*AnimatedVisibility(
                    visible = showAddToCartMessage,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(5f)
                ) {
                    AddedToCartSnackBar(
                        modifier = Modifier
                            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                            .zIndex(5f)
                    ) {
                        navController.navigate(Screen.Cart.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                }*/

                val product = productDetailState.data
                Log.e("CURRENT PRODUCT", "$product")
                val tabs = listOf("الوصف", "التقييمات")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    // Product Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.LightGray) // Placeholder background
                            .zIndex(0f)
                    ) {
                        if (product.thumbnail != null) {
                            AsyncImage(
                                model = product.thumbnail,
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            modifier = Modifier
                                .background(Color.Black, shape = CircleShape)
                                .size(32.dp)
                                .clip(CircleShape),
                            onClick = { navController.popBackStack() },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp),
                                contentDescription = null
                            )
                        }
                        FavouriteToggleIcon(size = 20.dp)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(1f)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(275.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(Color.White)
                                .padding(vertical = 25.dp)
                        ) {
                            TabRow(
                                selectedTabIndex = selectedTabIndex,
                                contentColor = Purple500,
                                containerColor = Color.White,
                                indicator = { tabPositions ->
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier
                                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                            .height(3.dp)
                                            .padding(horizontal = 16.dp),
                                        color = Purple500
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        selectedContentColor = Purple500,
                                        unselectedContentColor = Color.Gray,
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = { Text(text = title) }
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> {
                                    ProductDetailContent(
                                        product = product,
                                        cartState = cartState,
                                        showAddToCart = showAddToCartMessage,
                                        onAddToCart = { quantity ->
                                            cartViewModel.addToCart(productDetailState.data.productId, quantity)
                                            productDetailsViewModel.onAddToCartClicked()
                                        }
                                    )
                                }
                                1 -> {
                                    ProductDetailReviews(
                                        reviewsState = reviewsState,
                                        addReviewState = addReviewState,
                                        hasUserReviewed = hasUserReviewed,
                                        currentUser = currentUser,
                                        onAddReview = { rating, comment ->
                                            productDetailsViewModel.addReview(
                                                slug, rating, comment
                                            )
                                        },
                                        onClearAddedReviewState = {
                                            productDetailsViewModel.clearAddReviewState()
                                        }
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
