package com.example.luqtaecommerce

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.di.dataModule
import com.example.luqtaecommerce.di.domainModule
import com.example.luqtaecommerce.di.networkModule
import com.example.luqtaecommerce.di.validationModule
import com.example.luqtaecommerce.di.viewModelModule
import com.example.luqtaecommerce.presentation.navigation.LuqtaNavGraph
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.presentation.splash.SplashViewModel
import com.example.luqtaecommerce.ui.theme.LuqtaEcommerceTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private var pendingDeepLink: Pair<String, String>? = null

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        splashViewModel = getViewModel()

        // Show system splash until loading is done (Android 12+)
        installSplashScreen().setKeepOnScreenCondition {
            false
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // handle status bar ( white bg, black items (battery, wifi, etc.) )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        window.statusBarColor = Color.TRANSPARENT
        windowInsetsController.isAppearanceLightStatusBars = true

        handleDeepLink(intent)

        setContent {
            LuqtaEcommerceTheme {
                KoinContext {
                    LuqtaApp()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        data?.let {
            if (it.pathSegments.size >= 3 && it.pathSegments[0] == "activate") {
                val uid = it.pathSegments[1]
                val token = it.pathSegments[2]
                pendingDeepLink = uid to token
            }
        }
    }

    @Composable
    fun LuqtaApp() {
        navController = rememberNavController()

        // ✅ Safely trigger deep link navigation when navController is ready
        LaunchedEffect(Unit) {
            Log.e("Deep Link", "moving to deep link")
            pendingDeepLink?.let { (uid, token) ->
                navController.navigate("${Screen.Activation.route}/$uid/$token")
                pendingDeepLink = null
            }
        }

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                LuqtaNavGraph(navController)
            }
        }
    }
}