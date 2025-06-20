package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.luqtaecommerce.ui.theme.LightPrimary
@Composable
fun ShimmerCategoryItem(
    modifier: Modifier = Modifier
) {
    val brush = shimmerBrush()

    Card(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = LightPrimary,
                shape = CardDefaults.shape
            )
            .padding(vertical = 22.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circle shimmer for emoji/icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Rectangle shimmer for category name
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

@Composable
fun ShimmerCategoriesRow(
    count: Int = 4 // number of placeholders
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        repeat(count) {
            ShimmerCategoryItem(
                modifier = Modifier.weight(1f)
            )
        }
    }
}
