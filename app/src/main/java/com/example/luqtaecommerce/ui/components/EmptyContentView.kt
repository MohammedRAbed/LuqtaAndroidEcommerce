package com.example.luqtaecommerce.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder

@Composable
fun EmptyContentView(
    navController: NavController,
    @DrawableRes image: Int,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image) ,
            contentDescription = title,
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = description,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = GrayPlaceholder,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
        )
        LuqtaButton(text = "تصفح المنتجات") {
            navController.navigate(Screen.Categories.route) {
                launchSingleTop = true
            }
        }
    }
}