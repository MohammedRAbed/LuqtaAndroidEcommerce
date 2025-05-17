package com.example.luqtaecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash: Screen("splash")
    data object Home: Screen("home")
    data object Signup: Screen("signup")
    data object Login: Screen("login")
    data object ForgotPassword: Screen("forgot_password")
}