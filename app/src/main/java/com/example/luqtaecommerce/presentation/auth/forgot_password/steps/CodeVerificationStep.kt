package com.example.luqtaecommerce.presentation.auth.forgot_password.steps

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordFormState
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.theme.GrayFont
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun CodeVerificationStep(
    context: Context,
    viewModel: ForgotPasswordViewModel,
    forgotPasswordState: ForgotPasswordFormState
) {
    LaunchedEffect(forgotPasswordState.codeError) {
        forgotPasswordState.codeError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
    Column(Modifier.padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.email_verification),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.enter_verification_code),
            fontSize = 14.sp,
            color = GrayFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OTP Input Field
        VerificationCodeInput(
            codeValue = forgotPasswordState.code,
            onValueChange = { viewModel.onCodeChanged(it) },
            isValid = forgotPasswordState.isCodeValid
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.resend_code),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryCyan,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.onSendEmail() },
            textAlign = TextAlign.Center
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
                text = stringResource(R.string.proceed),
                onClick = { viewModel.onVerifyCode() }
            )
        }
    }
}

@Composable
private fun VerificationCodeInput(
    codeValue: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean? = null
) {
    val codeLength = 6
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            BasicTextField(
                value = codeValue,
                onValueChange = { newValue ->
                    //if (newValue.length <= codeLength && newValue.all { it.isDigit() }) {
                    onValueChange(newValue)
                    //}
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp) // or the height of your Row
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            repeat(codeLength) { index ->
                                val char = when {
                                    index < codeValue.length -> codeValue[index].toString()
                                    else -> ""
                                }
                                val isFilled = index < codeValue.length

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .border(
                                            width = 1.dp,
                                            color = when {
                                                codeValue.isEmpty() -> Color.Gray
                                                isValid == true -> PrimaryCyan
                                                isValid == false -> RedFont
                                                else -> if (isFilled) PrimaryCyan else Color.Gray
                                            },
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .background(
                                            color = Color.Transparent,
                                            shape = MaterialTheme.shapes.small
                                        )
                                ) {
                                    Text(
                                        text = char,
                                        style = MaterialTheme.typography.titleLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Properly layered invisible text field for cursor/input
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(0f) // Invisible but interactive
                        ) {
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = TextStyle(color = Color.Transparent)
            )
        }
    }
}