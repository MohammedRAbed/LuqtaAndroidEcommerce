package com.example.luqtaecommerce.presentation.navigation

import android.os.Build
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordScreen
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
        composable(
            route = Screen.Splash.route,
            enterTransition = null,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, // 👉 to right
                    animationSpec = tween(900)
                )
            },
            popEnterTransition = null,
            popExitTransition = null
        ) {
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(navController, splashViewModel)
        }

        rootComposable(route = Screen.Login.route){
            LoginScreen(navController)
        }

        rootComposable(route = Screen.Signup.route,) {
            SignupScreen(navController)
        }

        rootComposable(route = Screen.ForgotPassword.route,) {
            ForgotPasswordScreen(navController)
        }

        rootComposable(route = Screen.Main.route,) {
            MainScreen()
        }

/*
        mainComposable(
            route = Screen.ProductDetails.route+ "/{slug}"
        ){ backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug")
            ProductDetailsScreen(
                navController = navController,
                slug = slug!!
            )
        }
 */
    }
}

fun NavGraphBuilder.rootComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, // 👉 from left
                animationSpec = tween(600)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, // 👉 to right
                animationSpec = tween(600)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, // 👈 from right
                animationSpec = tween(600)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }, // 👈 to left
                animationSpec = tween(600)
            )
        },
        content = content
    )
}