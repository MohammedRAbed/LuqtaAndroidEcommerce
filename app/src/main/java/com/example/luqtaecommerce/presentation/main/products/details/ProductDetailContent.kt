package com.example.luqtaecommerce.presentation.main.products.details

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.presentation.main.cart.CartState
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.PrimaryCyan

@SuppressLint("DefaultLocale")
@Composable
fun ProductDetailContent(
    product: ProductDetails,
    cartState: CartState,
    showAddToCart: Boolean,
    onAddToCart: (quantity: Int) -> Unit
) {
    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }
    var showFullDescription by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {

        /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Chip(text = "موصى به", backgroundColor = Color(0xFFE0F7FA))
                Spacer(modifier = Modifier.width(8.dp))
                Chip(text = "الحالة جيدة", backgroundColor = Color(0xFFE8F5E9))
            }
        }*/

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                for (tag in product.tags) {
                    Chip(text = "#$tag", backgroundColor = LightPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${product.price}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Rating
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(product.rating.average.toInt()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_filled), // You'll need a filled star icon
                    contentDescription = "Star",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(10.dp)
                )
            }
            if (product.rating.average % 2 != 0.0) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_half_filled), // You'll need a filled star icon
                    contentDescription = "Star",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(10.dp)
                )
            }
            repeat(
                if (product.rating.average % 2 != 0.0)
                    5 - (product.rating.average.toInt() + 1)
                else 5 - product.rating.average.toInt()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_unfilled), // You'll need a filled star icon
                    contentDescription = "Star",
                    tint = Color(0xFFF4F5FD),
                    modifier = Modifier.size(10.dp)
                )
            }

            Text(
                text = " ${String.format("%.1f", product.rating.average)} (${product.rating.count} مراجعات)",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = GrayPlaceholder)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // Description
            val words = product.description.split(" ")
            val displayDescription = if (words.size > 20 && !showFullDescription) {
                words.take(20).joinToString(" ") + "..."
            } else {
                product.description
            }
            Text(
                text = displayDescription,
                fontSize = 16.sp,
                color = Color.DarkGray,
                lineHeight = 24.sp
            )
            if (words.size > 20 && !showFullDescription) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = { showFullDescription = !showFullDescription }) {
                        Text(
                            text = "قراءة المزيد",
                            color = PrimaryCyan,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

            // Quantity Selector
            Text(
                text = "الكمية",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Buy Now and Add to Cart Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(
                            color = LightPrimary,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    // Decrease button
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        enabled = quantity > 1
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_minus_en
                            ), // You'll need a minus icon
                            contentDescription = "Decrease quantity",
                            tint = if (quantity > 1) Color.Black else GrayPlaceholder
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = quantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Increase button
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus_en), // You'll need a plus icon
                            contentDescription = "Increase quantity",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))
                Button(
                    onClick = { onAddToCart(quantity) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !cartState.isLoading && !showAddToCart
                ) {
                    Text(
                        text = "إضافة إلى السلة",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.shopping_cart_button),
                        contentDescription = "Add to Cart",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun Chip(text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}