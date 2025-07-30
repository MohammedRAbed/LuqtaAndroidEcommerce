package com.example.luqtaecommerce.presentation.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.Purple500
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun ProfileScreen(
    outerNavController: NavController,
    navController: NavController,
    currentUser: User?,
    viewModel: ProfileViewModel = koinNavViewModel()
) {
    val isLoading by viewModel.isLoggingOut.collectAsState()

    // Observe logout result
    LaunchedEffect(Unit) {
        viewModel.logoutSuccess.collect {
            outerNavController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentUser?.profilePic != null) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = currentUser.profilePic,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text("الحساب الشخصي", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${currentUser?.username}", fontSize = 18.sp)

        Text(text = "${currentUser?.firstName} ${currentUser?.lastName}", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (currentUser?.profilePic == null) {
            Box(modifier = Modifier.width(150.dp)) {
                LuqtaButton(text = "إضافة صورة للحساب", enabled = !isLoading) {
                    navController.navigate(Screen.ProfilePic.route)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Box(modifier = Modifier.width(150.dp)) {
            LuqtaButton(text = "تسجيل الخروج", enabled = !isLoading) {
                viewModel.logout()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(color = Purple500)
            Spacer(modifier = Modifier.height(4.dp))
            Text("جاري تسجيل الخروج...")
        }
    }
}