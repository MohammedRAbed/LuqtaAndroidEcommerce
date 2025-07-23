package com.example.luqtaecommerce.presentation.main.orders

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.domain.model.order.OrderItem
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.ui.components.EmptyContentView
import com.example.luqtaecommerce.ui.components.LoadErrorView
import com.example.luqtaecommerce.ui.components.LuqtaBackHeader
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel = koinViewModel()
) {
    val ordersState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("قيد الانتظار", "مكتمل") // Use stringResource for localization

    Scaffold(
        topBar = {
            LuqtaBackHeader(title = "الطلبات", navController = navController)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp)
                            .padding(horizontal = 16.dp),
                        color = Color.Black
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray,
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            if (ordersState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryCyan)
                }
            } else if (ordersState.error != null) {
                LoadErrorView { viewModel.retryFetchingOrders() }

                Log.e("Orders Error", ordersState.error!!)
            } else {
                when (selectedTabIndex) {
                    0 -> OrderList(
                        orders = ordersState.ongoingOrders,
                        navController = navController
                    )

                    1 -> OrderList(
                        orders = ordersState.completedOrders,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun OrderList(orders: List<Order>, navController: NavController) {
    // Assuming one order card per order, and listing items within it.
    // The mockup seems to show one card per *item*, which is a bit different from the API response.
    // This implementation creates one card per order.

    if (orders.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                // For simplicity, let's display the first item of each order to match the mockup style
                OrderItemCard(
                    title = "طلبية (${formatOrderId(order.orderId)})", // This is in Arabic
                    price = order.totalPrice,
                    status = order.status,
                    date = order.orderDate // You'll want to format this date
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            EmptyContentView(
                navController = navController,
                image = R.drawable.ic_empty_orders,
                title = "قائمة الطلبات فارغة",
                description = "يمكنك إضافة الطلب عبر إتمام عملية الشراء من سلة المشتريات"
            )
        }
    }
}

@Composable
fun OrderItemCard(
    title: String,
    price: String,
    status: String,
    date: String
) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_order_single),
                    contentDescription = "Order",
                    modifier = Modifier.padding(end = 12.dp),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = "$$price",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                    // The +/- buttons in the mockup don't fit a history screen, so they are omitted.
                }
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = when (status) {
                            "Pending" -> Color(0xFFF5F5E8)
                            "Completed" -> Color(0xFFE7FAE0)
                            else -> Color(0xFFF5E8E8)
                        },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = when (status) {
                                "Completed" -> "مكتمل"
                                "Pending" -> "قيد الانتظار"
                                else -> "ملغى"
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = when (status) {
                                "Pending" -> Color(0xFF535301)
                                "Completed" -> Color(0xFF165300)
                                else -> Color(0xFF580000)
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        ) // E.g., "Finished", "Pending"
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = date.take(10),
                        color = Color.Gray,
                        fontSize = 12.sp
                    ) // Display YYYY-MM-DD
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

fun formatOrderId(orderId: String): String {
    return orderId.split("-").getOrNull(1) ?: orderId.substring(0,4)
}
