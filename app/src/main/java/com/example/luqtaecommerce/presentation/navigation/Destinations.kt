package com.example.luqtaecommerce.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash: Screen("splash")
    data object Home: Screen("home")
}