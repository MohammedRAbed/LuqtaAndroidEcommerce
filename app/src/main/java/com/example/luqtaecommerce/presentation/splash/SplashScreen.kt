package com.example.luqtaecommerce.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSplashScreen()
    }

    LaunchedEffect(isLoading) {
        if(!isLoading) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_temp_logo2),
            contentDescription = "App Icon" // Provide a meaningful content description
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    // Replace with a placeholder drawable resource from your project
    SplashScreen(rememberNavController(), SplashViewModel())
}