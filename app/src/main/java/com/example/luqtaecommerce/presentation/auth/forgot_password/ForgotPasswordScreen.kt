package com.example.luqtaecommerce.presentation.auth.forgot_password

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.auth.forgot_password.steps.CodeVerificationStep
import com.example.luqtaecommerce.presentation.auth.forgot_password.steps.ConfirmationEmailStep
import com.example.luqtaecommerce.presentation.auth.forgot_password.steps.NewPasswordStep
import com.example.luqtaecommerce.ui.theme.GrayFont
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val forgotPasswordState by viewModel.forgotPasswordState.collectAsState()
    val context = LocalContext.current

    // show Toast whenever these values change (success/fail)
    LaunchedEffect(forgotPasswordState.passwordSaved) {
        if (forgotPasswordState.passwordSaved) {
            Toast.makeText(
                context,
                context.getString(R.string.password_updated_successfully), Toast.LENGTH_SHORT
            ).show()

            /* Navigate to next screen */
            navController.popBackStack()
            // reset fields
            viewModel.resetForgotPasswordForm()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(end = 0.dp),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back"
                )
            }
            Text(
                text = stringResource(R.string.change_password_title),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f)) // Push the next item to the end
            Text(
                text = "0${forgotPasswordState.step + 1}", // Replace with your trailing text
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "/03", // Replace with your trailing text
                fontSize = 16.sp,
                color = GrayFont,
                fontWeight = FontWeight.Medium
            )
        }


        AnimatedContent(
            targetState = forgotPasswordState.step,
            transitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
            },
            label = "StepTransition"
        ) { step ->
            when (step) {
                0 -> {
                    // Email Step
                    ConfirmationEmailStep(
                        context = context,
                        viewModel = viewModel,
                        forgotPasswordState = forgotPasswordState
                    )
                }

                1 -> {
                    // Code Step
                    CodeVerificationStep(
                        context = context,
                        viewModel = viewModel,
                        forgotPasswordState = forgotPasswordState
                    )
                }

                2 -> {
                    // New Password Step
                    NewPasswordStep(
                        context = context,
                        viewModel = viewModel,
                        forgotPasswordState = forgotPasswordState
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenP() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ForgotPasswordScreen(rememberNavController())
    }
}