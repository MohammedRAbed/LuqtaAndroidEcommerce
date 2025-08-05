package com.example.luqtaecommerce.presentation.main


import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.luqtaecommerce.presentation.auth.AuthStateManager
import com.example.luqtaecommerce.ui.components.LuqtaBottomNavItem
import com.example.luqtaecommerce.presentation.main.cart.CartScreen
import com.example.luqtaecommerce.presentation.main.categories.CategoriesScreen
import com.example.luqtaecommerce.presentation.main.home.HomeScreen
import com.example.luqtaecommerce.presentation.main.products.details.ProductDetailsScreen
import com.example.luqtaecommerce.presentation.main.products.catalog.ProductSearchScreen
import com.example.luqtaecommerce.presentation.main.products.catalog.ProductsScreen
import com.example.luqtaecommerce.presentation.main.products.catalog.ProductsViewModel
import com.example.luqtaecommerce.presentation.main.profile.AddProfilePicScreen
import com.example.luqtaecommerce.presentation.main.profile.ProfileScreen
import com.example.luqtaecommerce.presentation.main.orders.OrdersScreen
import com.example.luqtaecommerce.presentation.main.orders.details.OrderDetailsScreen
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.presentation.navigation.bottomNavItems
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.Purple50
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    outerNavController: NavHostController,
    authStateManager: AuthStateManager = koinInject(),
) {
    val navController = rememberNavController()

    val userState by authStateManager.currentUser.collectAsState()

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val screenOrder = listOf(
        Screen.Home.route,
        Screen.Categories.route,
        Screen.Cart.route,
        Screen.Orders.route,
        Screen.Profile.route
    )

    val hiddenBarRoutes = listOf(
        Screen.Products.route,
        Screen.ProductDetails.route,
        Screen.OrderDetails.route
    )
    val hideBottomBar = hiddenBarRoutes.any { route ->
        currentRoute?.contains(route) == true
    }
    Log.i("currentRoute", "currentRoute: $currentRoute, isProductsScreen: $hiddenBarRoutes")

    var previousRoute by remember { mutableStateOf(Screen.Home.route) }

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar(
                    containerColor = Purple50,
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
                        //val isSelected = currentRoute == item.route
                        val isSelected = when {
                            currentRoute == item.route -> true
                            currentRoute?.startsWith(Screen.Products.route) == true && item.route == Screen.Categories.route -> true
                            else -> false
                        }
                        LuqtaBottomNavItem(
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
        }
    ) { innerPadding ->

        val productsViewModel: ProductsViewModel = koinNavViewModel()

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
        ) {

            screenOrder.forEach { route ->
                mainComposable(
                    route = route,
                    navController = navController,
                    screenOrder = screenOrder,
                    previousRoute = previousRoute
                ) {
                    LaunchedEffect(Unit) {
                        previousRoute = route
                    }

                    when (route) {
                        Screen.Home.route -> HomeScreen(navController, userState)
                        Screen.Categories.route -> CategoriesScreen(navController)
                        Screen.Cart.route -> CartScreen(navController)
                        Screen.Orders.route -> OrdersScreen(navController)
                        Screen.Profile.route -> ProfileScreen(
                            outerNavController = outerNavController,
                            navController = navController,
                            currentUser = userState
                        )
                    }
                }
            }

            productsComposable(
                route = Screen.Products.route + "/{categorySlug}/{categoryName}",
                argumentKeys = listOf("categorySlug", "categoryName"),
                previousRoute = previousRoute,
                navController = navController
            ) { backStackEntry ->
                val categorySlug = backStackEntry.arguments?.getString("categorySlug")
                val categoryName = backStackEntry.arguments?.getString("categoryName")
                LaunchedEffect(Unit) {
                    previousRoute = Screen.Products.route // track correctly
                }
                ProductsScreen(
                    navController = navController,
                    categorySlug = categorySlug,
                    categoryName = categoryName,
                    viewModel = productsViewModel
                )
            }

            productsComposable(
                route = Screen.Products.route,
                previousRoute = previousRoute,
                navController = navController
            ) {
                LaunchedEffect(Unit) {
                    previousRoute = Screen.Products.route // track correctly
                }
                ProductsScreen(
                    navController = navController,
                    viewModel = productsViewModel
                )
            }

            composable(
                route = Screen.ProductsSearch.route,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                popEnterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                popExitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(250)
                    )
                }
            ) {
                LaunchedEffect(Unit) {
                    previousRoute = Screen.ProductsSearch.route // track correctly
                }
                ProductSearchScreen(
                    navController = navController,
                    viewModel = productsViewModel
                )
            }

            composable(
                route = Screen.ProductDetails.route + "/{slug}",
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                popEnterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
                popExitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(250)
                    )
                },
            ) { backStackEntry ->
                LaunchedEffect(Unit) {
                    previousRoute = Screen.ProductDetails.route // track correctly
                }
                val slug = backStackEntry.arguments?.getString("slug")
                ProductDetailsScreen(
                    navController = navController,
                    slug = slug!!,
                    currentUser = userState
                )
            }


            composable(
                route = Screen.OrderDetails.route + "/{orderId}",
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
                }
            ) { backStackEntry ->
                LaunchedEffect(Unit) {
                    previousRoute = Screen.OrderDetails.route // track correctly
                }
                val orderId = backStackEntry.arguments?.getString("orderId")
                OrderDetailsScreen(
                    navController = navController,
                    orderId = orderId!!,
                )
            }

            composable(
                route = Screen.ProfilePic.route,
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
                }
            ) {
                AddProfilePicScreen(navController, userState)
            }
        }
    }
}

fun NavGraphBuilder.mainComposable(
    route: String,
    navController: NavHostController,
    screenOrder: List<String>,
    previousRoute: String,
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            val toRoute = route
            val direction = getTransitionDirection(screenOrder, previousRoute, toRoute)
            if (previousRoute.contains(Screen.ProductDetails.route)) {
                fadeIn()
            } else {
                slideInHorizontally(
                    initialOffsetX = { direction * it },
                    animationSpec = tween(400)
                )
            }
        },
        exitTransition = {
            val fromRoute = route
            val toRoute = navController.currentDestination?.route ?: ""
            val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
            if (toRoute.contains(Screen.ProductDetails.route)) {
                fadeOut()
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -direction * it },
                    animationSpec = tween(400)
                )
            }
        },
        popEnterTransition = {
            val toRoute = route
            val direction = getTransitionDirection(screenOrder, previousRoute, toRoute)
            if (previousRoute.contains(Screen.ProductDetails.route)) {
                fadeIn()
            } else {
                slideInHorizontally(
                    initialOffsetX = { direction * it },
                    animationSpec = tween(400)
                )
            }
        },
        popExitTransition = {
            val fromRoute = route
            val toRoute = navController.currentBackStackEntry?.destination?.route ?: ""
            val direction = getTransitionDirection(screenOrder, fromRoute, toRoute)
            if (toRoute.contains(Screen.ProductDetails.route)) {
                fadeOut()
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -direction * it },
                    animationSpec = tween(400)
                )
            }
        },
        content = content
    )
}

fun NavGraphBuilder.productsComposable(
    route: String,
    navController: NavHostController,
    previousRoute: String,
    argumentKeys: List<String> = emptyList(),
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        arguments = argumentKeys.map { navArgument(it) { type = NavType.StringType } },
        enterTransition = {
            if (previousRoute == Screen.Categories.route) {
                fadeIn(animationSpec = tween(600)) + scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(600)
                )
            } else null
        },
        exitTransition = {
            val to = navController.currentBackStackEntry?.destination?.route ?: ""
            if (to == Screen.Categories.route) {
                fadeOut(animationSpec = tween(300)) + scaleOut(
                    targetScale = 0.95f,
                    animationSpec = tween(300)
                )
            } else null
        },
        popEnterTransition = {
            if (previousRoute == Screen.Categories.route) {
                fadeIn(animationSpec = tween(300)) + scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(300)
                )
            } else null
        },
        popExitTransition = {
            val to = navController.currentBackStackEntry?.destination?.route ?: ""
            if (to == Screen.Categories.route) {
                fadeOut(animationSpec = tween(300)) + scaleOut(
                    targetScale = 0.95f,
                    animationSpec = tween(300)
                )
            } else null
        },
        content = content
    )
}

fun getTransitionDirection(order: List<String>, from: String, to: String): Int {
    val fromIndex = order.indexOf(from)
    val toIndex = order.indexOf(to)
    return when {
        toIndex == -1 -> -1
        fromIndex == -1 && toIndex > 1 -> -1
        fromIndex == -1 && toIndex <= 1 -> 1
        fromIndex < toIndex -> -1 // forward in list → slide left
        fromIndex > toIndex -> 1  // backward in list → slide right
        else -> 0
    }
}
