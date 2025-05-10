package com.example.luqtaecommerce

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.presentation.navigation.LuqtaNavGraph
import com.example.luqtaecommerce.presentation.splash.SplashViewModel
import com.example.luqtaecommerce.ui.theme.LuqtaEcommerceTheme

class MainActivity : ComponentActivity() {

     // private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show system splash until loading is done (Android 12+)
        installSplashScreen().setKeepOnScreenCondition {
            false // don't block UI, we'll handle splash inside app
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        window.statusBarColor = Color.parseColor("#FFFFFFFF")
        windowInsetsController.isAppearanceLightStatusBars = true

        setContent {
            LuqtaEcommerceTheme {
                LuqtaApp()
            }
        }
    }

    @Composable
    fun LuqtaApp() {
        val navController = rememberNavController()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val splashViewModel: SplashViewModel = viewModel()
            LuqtaNavGraph(navController, splashViewModel)
        } else {
            LuqtaNavGraph(navController, null)
        }

    }
}