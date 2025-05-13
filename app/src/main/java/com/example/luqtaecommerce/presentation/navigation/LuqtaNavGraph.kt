package com.example.luqtaecommerce.presentation.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luqtaecommerce.presentation.home.HomeScreen
import com.example.luqtaecommerce.presentation.signup.SignupScreen
import com.example.luqtaecommerce.presentation.splash.SplashScreen
import com.example.luqtaecommerce.presentation.splash.SplashViewModel

@Composable
fun LuqtaNavGraph(
    navController: NavHostController
) {

    val isPreAndroid12 = Build.VERSION.SDK_INT < Build.VERSION_CODES.S

    NavHost(
        navController = navController,
        startDestination = if (isPreAndroid12) Screen.Splash.route else Screen.Signup.route
    ) {
        // Only for Android < 22 (SDK < 31)
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(Screen.Signup.route) {
            SignupScreen()
        }

        composable(Screen.Home.route) { HomeScreen() }
    }
}