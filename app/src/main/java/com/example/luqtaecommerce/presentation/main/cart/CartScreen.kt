package com.example.luqtaecommerce.presentation.main.cart

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.EmptyContentView
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.luqtaecommerce.domain.model.cart.CartItem
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.LightPrimary
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.luqtaecommerce.ui.components.LuqtaTextField
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = koinViewModel()
) {
    val state = viewModel.cartState.collectAsState().value
    val sheetState = rememberModalBottomSheetState()

    val paymentUrl by viewModel.paymentUrl.collectAsState()
    val isCheckingOut by viewModel.isCheckingOut.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.e("CartScreen", "LaunchedEffect")
        viewModel.loadCart()
    }

    LaunchedEffect(state.operationStatus) {
        if (state.operationStatus == CartOperationStatus.REMOVE_SUCCESS) {
            Toast.makeText(context, "تم حذف المنتج من السلة ❌🛒", Toast.LENGTH_SHORT).show()
            viewModel.loadCart()
        }
    }

    // This effect launches the browser when the paymentUrl is received
    LaunchedEffect(paymentUrl) {
        paymentUrl?.let { url ->
            //...
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Cart.route) { inclusive = true }
                launchSingleTop = true
            }

            delay(100)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            viewModel.onPaymentUrlLaunched() // Reset the state
        }
    }

    if (state.isCouponSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideCouponSheet() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("أدخل رمز الكوبون", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                LuqtaTextField(
                    value = state.couponCode,
                    onValueChange = { viewModel.onCouponCodeChange(it) },
                    placeholder = "رمز الكوبون"
                )
                if (state.couponError != null) {
                    Text(state.couponError, color = RedFont, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LuqtaButton(
                        text = "إلغاء",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.hideCouponSheet() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    LuqtaButton(
                        text = "تطبيق",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.applyCoupon() },
                        enabled = !state.isLoading
                    )
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(color = PrimaryCyan, modifier = Modifier.align(Alignment.Center))
            }
            state.error != null -> {
                LoadErrorView(onClick = { viewModel.loadCart() })
            }
            state.cart == null || state.cart.items.isEmpty() -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                ) {
                    EmptyContentView(
                        navController = navController,
                        image = com.example.luqtaecommerce.R.drawable.ic_empty_cart,
                        title = "سلة المشتريات فارغة",
                        description = "يبدو أنك لم تضف منتجات إلى سلة التسوق الخاصة بك، بإمكانك مواصلة التسوق وتصفح المنتجات"
                    )
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "سلة المشتريات",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "كوبون الخصم",
                            color = if(state.cart.coupon == null) PrimaryCyan else GrayFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            modifier = if(state.cart.coupon == null) {
                                Modifier.clickable { viewModel.showCouponSheet() }
                            } else { Modifier }
                        )
                    }
                    // Cart Items
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.cart.items) { cartItem ->
                            val productId = cartItem.product.productId
                            CartItemRow(
                                cartItem = cartItem,
                                onDelete = { viewModel.removeFromCart(productId) }
                            )
                            HorizontalDivider(color = GrayPlaceholder, thickness = 1.dp)
                        }
                    }
                    // Order Info & Checkout
                    Surface(
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = "معلومات الطلب",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if(state.cart.coupon != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المجموع (بدون الخصم)", color = GrayFont, fontSize = 15.sp)
                                    Text(
                                        "${state.cart.totalPrice.withoutDiscount + 0.0}",
                                        fontWeight = FontWeight.Medium,
                                        color = GrayFont,
                                        fontSize = 15.sp,
                                        style = TextStyle(
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المجموع (مع الخصم)", color = GrayFont, fontSize = 15.sp)
                                    Text(
                                        "${state.cart.totalPrice.withDiscount + 0.0}",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المجموع", color = GrayFont, fontSize = 15.sp)
                                    Text(
                                        "${state.cart.totalPrice.withDiscount + 0.0}",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("تكلفة الشحن", color = GrayFont, fontSize = 15.sp)
                                Text("$0.00", color = Color.Black, fontSize = 15.sp)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("الإجمالي", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                Text(
                                    "$${state.cart.totalPrice.withDiscount}",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                            LuqtaButton(
                                text = "تأكيد الطلب (${state.cart.items.size})",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                onClick = { viewModel.onCheckoutClicked() },
                                enabled = !isCheckingOut
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(cartItem: CartItem, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    if (cartItem.product.thumbnail == null)
                        Color.LightGray
                    else
                        Color.Transparent
                )
                .zIndex(0f)
        ) {
            if (cartItem.product.thumbnail != null) {
                AsyncImage(
                    model = cartItem.product.thumbnail
                        ?: "https://th.bing.com/th/id/R.ed0bf03e1a684437be6d46f7d420d239?rik=j59a58Mk3c9mnw&pid=ImgRaw&r=0", // Use a placeholder if no thumbnail ,
                    contentDescription = cartItem.product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        // Product Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${cartItem.product.price}",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (cartItem.product.price != cartItem.totalPrice) {
                Text(
                    text = cartItem.totalPrice,
                    color = GrayFont,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            // Quantity controls (stub)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                IconButton(enabled = false, onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus_en),
                        contentDescription = "Decrease"
                    )
                }
                Text(
                    text = cartItem.quantity.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = GrayFont
                )
                IconButton(enabled = false, onClick = {}) {
                    Icon(
                        painter = painterResource(id = com.example.luqtaecommerce.R.drawable.ic_plus_en),
                        contentDescription = "Increase"
                    )
                }
            }
        }
        // Delete button
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = com.example.luqtaecommerce.R.drawable.ic_trash),
                contentDescription = "Remove from cart",
                tint = RedFont,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}