package com.example.luqtaecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash: Screen("splash")

    data object Signup: Screen("signup")
    data object Login: Screen("login")
    data object ForgotPassword: Screen("forgot_password")
    data object CheckEmail: Screen("check_email")
    data object Activation: Screen("activation")

    // Bottom Nav Screens
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object Categories : Screen("categories")
    data object Cart : Screen("cart")
    data object Orders : Screen("orders")
    data object Profile : Screen("profile")

    // Products
    data object Products : Screen("products")
    data object ProductDetails : Screen("product_details")
    data object ProductsSearch : Screen("products_search")

    data object OrderDetails : Screen("order_details")

    // Profile
    data object ProfilePic: Screen("profile_pic")
}