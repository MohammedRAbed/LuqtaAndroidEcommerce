package com.example.luqtaecommerce.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luqtaecommerce.ui.theme.GrayPlaceholder
import com.example.luqtaecommerce.ui.theme.LightPrimary
import com.example.luqtaecommerce.ui.theme.Purple500
import com.example.luqtaecommerce.ui.theme.RedFont

@Composable
fun LuqtaTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isValid: Boolean? = null,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = GrayPlaceholder, fontSize = 14.sp) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Purple500,
            unfocusedBorderColor = when {
                value.isEmpty() -> LightPrimary
                isValid == true -> Purple500
                else -> RedFont
            },
            cursorColor = Purple500
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