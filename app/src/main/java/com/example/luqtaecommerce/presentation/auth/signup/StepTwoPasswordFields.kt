package com.example.luqtaecommerce.presentation.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.components.LuqtaPasswordTextField
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun StepTwoPasswordFields(
    viewModel: SignupViewModel,
) {
    val signupState by viewModel.signupState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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

        Spacer(modifier = Modifier.height(16.dp))
    }
}