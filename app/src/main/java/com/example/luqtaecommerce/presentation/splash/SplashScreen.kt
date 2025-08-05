package com.example.luqtaecommerce.presentation.splash

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.data.remote.NetworkUtil
import com.example.luqtaecommerce.presentation.auth.AuthState
import com.example.luqtaecommerce.presentation.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var isCheckingHost by remember { mutableStateOf(true) }
    var retryTrigger by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(Unit, retryTrigger) {
        if (isCheckingHost) {
            val isReachable = withContext(Dispatchers.IO) {
                NetworkUtil.isHostReachable("192.168.1.200") ||
                        NetworkUtil.isHostReachable("10.0.2.2")
            }

            isCheckingHost = false
            if (!isReachable) {
                showDialog = true
            } else {
                viewModel.initAuthCheck()
            }

            retryTrigger = false
        }
    }

    LaunchedEffect(authState) {
        Log.e("SplashScreen", "navigationDestination: $authState")
        when (authState) {
            is AuthState.Loading -> {}
            is AuthState.Authenticated -> {
                delay(300)
                navController.navigate(Screen.Main.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                delay(300)
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_luqta_logo),
            contentDescription = "App Icon" // Provide a meaningful content description
        )
        if (isCheckingHost) {
            Spacer(modifier = Modifier.height(50.dp))
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("حصل خلل في الاتصال") },
                text = { Text("تعذّر الوصول إلى الخادم. يُرجى التحقق من اتصالك والمحاولة مرة أخرى.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        isCheckingHost = true
                        retryTrigger = true
                    }) {
                        Text("المحاولة مرة أخرى")
                    }
                },
                dismissButton = {
                    val activity = (LocalContext.current as? Activity)
                    TextButton(onClick = {
                        showDialog = false
                        // Optionally close the app or stay on splash screen
                        activity?.finish()
                    }) {
                        Text("الخروج")
                    }
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    // Replace with a placeholder drawable resource from your project
    //SplashScreen(rememberNavController(), SplashViewModel())
}