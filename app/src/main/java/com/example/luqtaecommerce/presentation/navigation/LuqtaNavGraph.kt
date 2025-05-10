package com.example.luqtaecommerce.presentation.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luqtaecommerce.presentation.home.HomeScreen
import com.example.luqtaecommerce.presentation.splash.SplashScreen
import com.example.luqtaecommerce.presentation.splash.SplashViewModel

@Composable
fun LuqtaNavGraph(
    navController: NavHostController,
    splashViewModel: SplashViewModel?
) {

    val isPreAndroid12 = Build.VERSION.SDK_INT < Build.VERSION_CODES.S

    NavHost(
        navController = navController,
        startDestination = if (isPreAndroid12) Screen.Splash.route else Screen.Home.route

    ) {
        // Only for Android < 22 (SDK < 31)
        composable(Screen.Splash.route) {
            SplashScreen(navController, splashViewModel!!)
        }

        composable(Screen.Home.route) { HomeScreen() }
    }
}