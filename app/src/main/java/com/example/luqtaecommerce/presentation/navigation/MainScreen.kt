package com.example.luqtaecommerce.presentation.navigation


import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.presentation.main.categories.CategoriesScreen
import com.example.luqtaecommerce.presentation.main.home.HomeScreen
import com.example.luqtaecommerce.presentation.main.profile.ProfileScreen
import com.example.luqtaecommerce.presentation.main.watchlist.WatchlistScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val screenOrder = listOf(
        Screen.Home.route,
        Screen.Categories.route,
        Screen.Watchlist.route,
        Screen.Profile.route
    )

    var previousRoute by remember { mutableStateOf(Screen.Home.route) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(
                                painter = painterResource(
                                    id = if (currentRoute == item.route) item.selectedIcon else item.icon
                                ),
                                contentDescription = item.title)
                               },
                        label = { Text(item.title) }
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
                        Screen.Categories.route -> CategoriesScreen()
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
