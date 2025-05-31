package com.example.luqtaecommerce.presentation.auth.signup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // show Toast whenever these values change (success/fail)
    LaunchedEffect(signupState.signupSuccessful) {
        if (signupState.signupSuccessful) {
            Toast.makeText(context,
                context.getString(R.string.signup_successful), Toast.LENGTH_SHORT).show()

            /* Navigate to next screen */
            navController.popBackStack()

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.full_name),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Text(
                text = "*",
                fontWeight = FontWeight.Medium,
                color = RedFont
            )
        }

        LuqtaTextField(
            value = signupState.fullName,
            onValueChange = { viewModel.onFullNameChange(it) },
            placeholder = stringResource(R.string.full_name_example),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isValid = signupState.isFullNameValid,
            errorMessage = signupState.fullNameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.email),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Text(
                text = "*",
                fontWeight = FontWeight.Medium,
                color = RedFont
            )
        }

        LuqtaTextField(
            value = signupState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = stringResource(R.string.email_example),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            isValid = signupState.isEmailValid,
            errorMessage = signupState.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.password),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Text(
                text = "*",
                fontWeight = FontWeight.Medium,
                color = RedFont
            )
        }

        LuqtaPasswordTextField(
            value = signupState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = stringResource(id = R.string.enter_password),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            imeAction = ImeAction.Next,
            isValid = signupState.isPasswordValid,
            errorMessage = signupState.passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.confirm_password),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Text(
                text = "*",
                fontWeight = FontWeight.Medium,
                color = RedFont
            )
        }

        LuqtaPasswordTextField(
            value = signupState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            placeholder = stringResource(R.string.enter_password),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            imeAction = ImeAction.Done,
            isValid = signupState.isConfirmPasswordValid,
            errorMessage = signupState.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(32.dp))

        if(signupState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = PrimaryCyan
            )
        } else {
            LuqtaButton(
                text = stringResource(R.string.add_account),
                onClick = { viewModel.onSignup() }
            )
        }


        // Show the error
        //signupState.signupError?.let { error ->
            Text(
                text = "${signupState.fullName}\n${signupState.email}\n${signupState.password}\n${signupState.confirmPassword}",
                fontWeight = FontWeight.Medium,
                color = RedFont,
                modifier = Modifier.padding(top = 8.dp)
            )
        //}

    }
}


@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl){
        LuqtaEcommerceTheme {
            val viewModel: SignupViewModel = viewModel()
            SignupScreen(rememberNavController(), viewModel)
        }
    }
}