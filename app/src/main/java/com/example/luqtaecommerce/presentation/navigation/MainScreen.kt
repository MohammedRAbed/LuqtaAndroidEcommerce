package com.example.luqtaecommerce.presentation.navigation


import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.presentation.main.CustomBottomNavItem
import com.example.luqtaecommerce.presentation.main.cart.CartScreen
import com.example.luqtaecommerce.presentation.main.categories.CategoriesScreen
import com.example.luqtaecommerce.presentation.main.home.HomeScreen
import com.example.luqtaecommerce.presentation.main.profile.ProfileScreen
import com.example.luqtaecommerce.presentation.main.watchlist.WatchlistScreen
import com.example.luqtaecommerce.ui.theme.LightPrimary

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val screenOrder = listOf(
        Screen.Home.route,
        Screen.Categories.route,
        Screen.Cart.route,
        Screen.Watchlist.route,
        Screen.Profile.route
    )

    var previousRoute by remember { mutableStateOf(Screen.Home.route) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val y = 0f // Draw at the top edge
                        drawLine(
                            color = LightPrimary,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    CustomBottomNavItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            screenOrder.forEach { route ->
                composable(
                    route = route,
                    enterTransition = {
                        val fromRoute = previousRoute
                        val toRoute = route
                        val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
                        slideInHorizontally(
                            initialOffsetX = { direction * it },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        val fromRoute = route
                        val toRoute = navController.currentDestination?.route ?: ""
                        val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
                        slideOutHorizontally(
                            targetOffsetX = { -direction * it },
                            animationSpec = tween(400)
                        )
                    },
                    popEnterTransition = {
                        val fromRoute = previousRoute
                        val toRoute = route
                        val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
                        slideInHorizontally(
                            initialOffsetX = { direction * it },
                            animationSpec = tween(400)
                        )
                    },
                    popExitTransition = {
                        val fromRoute = route
                        val toRoute = navController.currentBackStackEntry?.destination?.route ?: ""
                        val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
                        slideOutHorizontally(
                            targetOffsetX = { -direction * it },
                            animationSpec = tween(400)
                        )
                    }
                ) {
                    LaunchedEffect(Unit) {
                        previousRoute = route
                    }

                    when (route) {
                        Screen.Home.route -> HomeScreen()
                        Screen.Categories.route -> CategoriesScreen(navController)
                        Screen.Cart.route -> CartScreen()
                        Screen.Watchlist.route -> WatchlistScreen()
                        Screen.Profile.route -> ProfileScreen()
                    }
                }
            }
        }
    }
}

fun getTransitionDirection(order: List<String>, from: String, to: String): Int {
    val fromIndex = order.indexOf(from)
    val toIndex = order.indexOf(to)
    return when {
        fromIndex < toIndex -> -1 // forward in list → slide left
        fromIndex > toIndex -> 1  // backward in list → slide right
        else -> 0
    }
}
