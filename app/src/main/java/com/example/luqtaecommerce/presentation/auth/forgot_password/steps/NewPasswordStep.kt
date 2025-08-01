package com.example.luqtaecommerce.presentation.auth.forgot_password.steps

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordFormState
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.components.LuqtaPasswordTextField
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.Purple500
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun NewPasswordStep(
    context: Context,
    viewModel: ForgotPasswordViewModel,
    forgotPasswordState: ForgotPasswordFormState
) {

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(forgotPasswordState.newPasswordError) {
        forgotPasswordState.newPasswordError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Column(Modifier.padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.new_password),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.enter_new_password),
            fontSize = 14.sp,
            color = GrayFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
            value = forgotPasswordState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            placeholder = stringResource(R.string.enter_password),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            imeAction = ImeAction.Done,
            isValid = forgotPasswordState.isPasswordValid,
            errorMessage = forgotPasswordState.passwordError
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
            value = forgotPasswordState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            placeholder = stringResource(R.string.confirm_password),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            imeAction = ImeAction.Done,
            isValid = forgotPasswordState.isConfirmPasswordValid,
            errorMessage = forgotPasswordState.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(24.dp))


        if(forgotPasswordState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = Purple500
            )
        } else {
            LuqtaButton(
                text = stringResource(R.string.save),
                onClick = { viewModel.onSavePassword() }
            )
        }
    }
}