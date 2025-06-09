package com.example.luqtaecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash: Screen("splash")
    data object Signup: Screen("signup")
    data object Login: Screen("login")
    data object ForgotPassword: Screen("forgot_password")

    // Bottom Nav Screens
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object Categories : Screen("categories")
    data object Cart : Screen("cart")
    data object Watchlist : Screen("watchlist")
    data object Profile : Screen("profile")
    // Products
    data object Products : Screen("products")
    data object ProductDetails : Screen("product_details")
}