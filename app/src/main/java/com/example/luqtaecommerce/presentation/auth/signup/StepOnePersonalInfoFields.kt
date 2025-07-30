package com.example.luqtaecommerce.presentation.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.components.LuqtaButton
import com.example.luqtaecommerce.ui.components.LuqtaTextField
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun StepOnePersonalFields(viewModel: SignupViewModel) {
    val signupState by viewModel.signupState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                LuqtaTextField(
                    value = signupState.firstName,
                    onValueChange = { viewModel.onFirstNameChange(it) },
                    placeholder = "الاسم الأول",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    isValid = signupState.isFirstNameValid,
                    errorMessage = signupState.firstNameError
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.weight(1f)) {
                LuqtaTextField(
                    value = signupState.lastName,
                    onValueChange = { viewModel.onLastNameChange(it) },
                    placeholder = "اسم العائلة",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    isValid = signupState.isLastNameValid,
                    errorMessage = signupState.lastNameError
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "اسم المستخدم",
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
            value = signupState.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            placeholder = "مثال: mo_username",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isValid = signupState.isUsernameValid,
            errorMessage = signupState.usernameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "رقم الهاتف",
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
            value = signupState.phoneNumber,
            onValueChange = { viewModel.onPhoneNumberChange(it) },
            placeholder = "+972 591 234 598",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            isValid = signupState.isPhoneNumberValid,
            errorMessage = signupState.phoneNumberError
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
    }
}