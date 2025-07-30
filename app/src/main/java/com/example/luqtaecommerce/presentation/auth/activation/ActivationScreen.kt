package com.example.luqtaecommerce.presentation.auth.activation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.Purple500
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivationScreen(
    uid: String,
    token: String,
    navController: NavController
) {
    val viewModel: ActivationViewModel = koinViewModel()
    val activationState by viewModel.activationState.collectAsState()

    LaunchedEffect(uid, token) {
        viewModel.activateAccount(uid, token)
    }

    // UI showing loading, success, or error states
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            activationState.isLoading -> {
                CircularProgressIndicator(color = Purple500)
                Spacer(modifier = Modifier.height(16.dp))
                Text("جاري تفعيل الحساب...")
            }

            activationState.isSuccess -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_success),
                    modifier = Modifier.size(200.dp),
                    contentDescription = "success"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "تم تفعيل الحساب بنجاح!",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("يمكنك الآن تسجيل الدخول وتصفح المنتجات", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                LuqtaButton(
                    text = "الانتقال لتسجيل الدخول",
                    onClick = {
                        viewModel.reset()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

            }

            activationState.error != null -> {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                Text("فشل التفعيل: ${activationState.error ?: "حدث خطأ ما"}", color = Color.Red)
            }
        }
    }
}