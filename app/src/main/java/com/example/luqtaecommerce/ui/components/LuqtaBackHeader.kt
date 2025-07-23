package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.R

@Composable
fun LuqtaBackHeader(title: String, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "Back"
            )
        }
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    }
}