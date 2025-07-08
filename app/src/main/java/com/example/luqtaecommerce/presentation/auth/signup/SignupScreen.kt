package com.example.luqtaecommerce.presentation.auth.signup

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.navigation.Screen
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.components.LuqtaPasswordTextField
import com.example.luqtaecommerce.ui.components.LuqtaTextField
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LuqtaEcommerceTheme
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = koinViewModel()
) {
    val signupState by viewModel.signupState.collectAsState()
    val context = LocalContext.current

    // show Toast whenever these values change (success/fail)
    LaunchedEffect(signupState.signupSuccessful) {
        if (signupState.signupSuccessful) {
            Toast.makeText(
                context,
                context.getString(R.string.signup_successful), Toast.LENGTH_SHORT
            ).show()

            /* Navigate to next screen */
            navController.navigate("${Screen.CheckEmail.route}/${signupState.email}")

            // reset fields
            viewModel.resetSignupForm()
        }
    }
    LaunchedEffect(signupState.signupError) {
        signupState.signupError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 35.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.create_new_account),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = GrayFont
            )
            Text(
                text = stringResource(R.string.login),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryCyan,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }

        AnimatedContent(
            targetState = signupState.currentStep,
            transitionSpec = {
                if(initialState < targetState)
                    slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                else
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
            },
            label = "StepTransition"
        ) { step ->
            when (step) {
                1 -> StepOnePersonalFields(viewModel)
                2 -> StepTwoPasswordFields(viewModel)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (signupState.currentStep > 1) {
                Button(
                    onClick = { viewModel.previousStep() },
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            color = Color.LightGray,
                            width = 1.dp,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                ) {
                    Text(
                        text = "رجوع",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 11.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (signupState.currentStep <= 2) {
                if (signupState.currentStep == 2) {
                    LuqtaButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.add_account),
                        onClick = { viewModel.onSignup() },
                        enabled = !signupState.isLoading
                    )
                } else {
                    Button(
                        onClick = { viewModel.nextStep() },
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                color = Color.LightGray,
                                width = 1.dp,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "التالي",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 11.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        if(signupState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = PrimaryCyan
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LuqtaEcommerceTheme {
            val viewModel: SignupViewModel = viewModel()
            SignupScreen(rememberNavController(), viewModel)
        }
    }
}