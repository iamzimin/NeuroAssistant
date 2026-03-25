package com.evg.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.evg.resource.R
import com.evg.ui.Keyboard
import com.evg.ui.keyboardAsState
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationTextField(
    filedName: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isPassword: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardState by keyboardAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible) painterResource(R.drawable.eye_off) else painterResource(R.drawable.eye_on)
    val keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
    val visualTransformation =
        if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None

    LaunchedEffect(keyboardState) {
        if (keyboardState == Keyboard.Closed) {
            focusManager.clearFocus()
        }
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(filedName, color = AppTheme.colors.text.copy(alpha = 0.6f)) },
        placeholder = {
            Text(
                text = placeholder,
                style = AppTheme.typography.body,
                color = AppTheme.colors.textFieldPlaceholder,
            )
        },
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = AppTheme.colors.textFieldPlaceholder,
                    )
                }
            }
        },
        textStyle = AppTheme.typography.body.copy(color = AppTheme.colors.text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.colors.primary,
            unfocusedBorderColor = AppTheme.colors.tileBackground,
            cursorColor = AppTheme.colors.primary,
            focusedContainerColor = AppTheme.colors.tileBackground,
            unfocusedContainerColor = AppTheme.colors.tileBackground,
            errorContainerColor = AppTheme.colors.tileBackground,
        )
    )
}


@Composable
@Preview(showBackground = true)
fun AuthorizationTextFieldPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            AuthorizationTextField(
                filedName = "Email",
                placeholder = "Enter your email",
                value = "test.email@mail.com",
                onValueChange = {},
                isError = false,
                isPassword = true,
            )
        }
    }
}
