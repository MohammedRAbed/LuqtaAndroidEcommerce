package com.example.luqtaecommerce.presentation.main.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.components.EmptyContentView

@Composable
fun WatchlistScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        EmptyContentView(
            navController = navController,
            image = R.drawable.ic_empty_wishlist,
            title = "قائمة المفضلة فارغة",
            description = "اضغط على زر القلب لبدء حفظ العناصر المفضلة لديك"
        )
    }
}