package com.example.luqtaecommerce.presentation.navigation

import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordScreen
import com.example.luqtaecommerce.presentation.home.HomeScreen
import com.example.luqtaecommerce.presentation.auth.login.LoginScreen
import com.example.luqtaecommerce.presentation.auth.signup.SignupScreen
import com.example.luqtaecommerce.presentation.splash.SplashScreen
import com.example.luqtaecommerce.presentation.splash.SplashViewModel

@Composable
fun LuqtaNavGraph(
    navController: NavHostController
) {

    val isPreAndroid12 = Build.VERSION.SDK_INT < Build.VERSION_CODES.S

    NavHost(
        navController = navController,
        startDestination = if (isPreAndroid12) Screen.Splash.route else Screen.Login.route
    ) {
        // Only for Android < 22 (SDK < 31)
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(
            route = Screen.Signup.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, // from right (forward nav)
                    animationSpec = tween(900)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, // to left (forward nav)
                    animationSpec = tween(900)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, // from left (backward nav)
                    animationSpec = tween(900)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, // to right (back nav)
                    animationSpec = tween(900)
                )
            }
        ) {
            SignupScreen(navController)
        }

        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, // from left (backward nav)
                    animationSpec = tween(900)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, // to left (forward nav)
                    animationSpec = tween(900)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, // from left (backward nav)
                    animationSpec = tween(900)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, // to right (back nav)
                    animationSpec = tween(900)
                )
            }
        ) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.ForgotPassword.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, // from right (forward nav)
                    animationSpec = tween(900)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, // to left (forward nav)
                    animationSpec = tween(900)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, // from left (backward nav)
                    animationSpec = tween(900)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, // to right (back nav)
                    animationSpec = tween(900)
                )
            }
        ) {
            ForgotPasswordScreen(navController)
        }

        composable(Screen.Home.route) { HomeScreen() }
    }
}