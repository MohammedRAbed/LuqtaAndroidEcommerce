package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.luqtaecommerce.ui.theme.LightPrimary

@Composable
fun ShimmerProductItem(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(color = LightPrimary, width = 1.dp, shape = RoundedCornerShape(24.dp))
            .padding(8.dp)
            //.shadow(1.dp, RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(brush, RoundedCornerShape(24.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(20.dp)
                .background(brush, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .background(brush, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ShimmerProductsGrid(count: Int = 6) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(count) {
            ShimmerProductItem()
        }
    }
}

