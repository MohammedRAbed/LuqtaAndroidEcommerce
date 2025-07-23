package com.example.luqtaecommerce.presentation.navigation

import androidx.annotation.DrawableRes
import com.example.luqtaecommerce.R

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        "الرئيسية",
        R.drawable.ic_home,
        R.drawable.ic_home_selected,
        Screen.Home.route
    ),
    BottomNavItem(
        "التصنيفات",
        R.drawable.ic_categories,
        R.drawable.ic_categories_selected,
        Screen.Categories.route
    ),
    BottomNavItem(
        "السلة",
        R.drawable.ic_cart,
        R.drawable.ic_cart_selected,
        Screen.Cart.route
    ),
    BottomNavItem(
        "الطلبات",
        R.drawable.ic_orders,
        R.drawable.ic_orders_selected,
        Screen.Orders.route
    ),
    BottomNavItem(
        "الحساب",
        R.drawable.ic_profile,
        R.drawable.ic_profile_selected,
        Screen.Profile.route
    )
)
