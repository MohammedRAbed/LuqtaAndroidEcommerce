package com.example.luqtaecommerce.presentation.auth.activation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan

@Composable
fun CheckEmailScreen(
    email: String?,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            tint = PrimaryCyan,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "تحقق من بريدك الإلكتروني",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "أرسلنا لك رابط تفعيل إلى بريدك الإلكتروني.\n ${maskEmail(email!!)} \n الرجاء النقر عليه لتفعيل حسابك.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = GrayFont
        )

        Spacer(modifier = Modifier.height(32.dp))

        LuqtaButton(
            text = "فتح تطبيق البريد",
            onClick = {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                }
                try {
                    navController.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(navController.context, "لا يوجد تطبيق بريد مثبت", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

fun maskEmail(email: String): String {
    val parts = email.split("@")
    if (parts.size != 2) return email

    val name = parts[0]
    val domain = parts[1]

    val visibleChars = 2.coerceAtMost(name.length)
    val maskedName = name.take(visibleChars) + "*".repeat((name.length - visibleChars).coerceAtLeast(1))

    return "$maskedName@$domain"
}