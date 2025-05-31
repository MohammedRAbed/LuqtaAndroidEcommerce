package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R

@Composable
fun LoadErrorView(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_error),
            modifier = Modifier.size(200.dp),
            contentDescription = "Error"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "حدث خطأ أثناء التحميل.",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            onClick = onClick
        ) {
            Text("إعادة المحاولة")
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Retry")
        }
    }
}