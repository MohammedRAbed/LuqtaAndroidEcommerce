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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordFormState
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.components.LuqtaTextField
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun ConfirmationEmailStep(
    context: Context,
    viewModel: ForgotPasswordViewModel,
    forgotPasswordState: ForgotPasswordFormState
) {
    LaunchedEffect(forgotPasswordState.emailSentError) {
        forgotPasswordState.emailSentError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
    Column(Modifier.padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.email_confirmation),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.enter_confirmation_email),
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
            value = forgotPasswordState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            placeholder = stringResource(R.string.email_example),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            isValid = forgotPasswordState.isEmailValid,
            errorMessage = forgotPasswordState.emailError
        )

        Spacer(modifier = Modifier.height(24.dp))


        if(forgotPasswordState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = PrimaryCyan
            )
        } else {
            LuqtaButton(
                text = stringResource(R.string.send),
                onClick = { viewModel.onSendEmail() }
            )
        }
    }
}