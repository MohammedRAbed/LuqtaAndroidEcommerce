package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.R
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.PrimaryCyan
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun LuqtaPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    isValid: Boolean? = null,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = GrayPlaceholder, fontSize = 14.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    painter = if (passwordVisible) painterResource(id = R.drawable.visible_icon) else painterResource(id = R.drawable.hidden_icon),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = when {
                value.isEmpty() -> Color.Gray
                isValid == true -> PrimaryCyan
                else -> RedFont
            },
            cursorColor = PrimaryCyan
        )
    )

    errorMessage?.let {
        Text(
            text = "* $it",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
        )
    }
}