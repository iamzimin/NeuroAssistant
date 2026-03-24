package com.evg.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = AppTheme.typography.body.copy(color = AppTheme.colors.text),
        label = label?.let {
            {
                Text(
                    text = it,
                    color = AppTheme.colors.textFieldPlaceholder,
                )
            }
        },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    color = AppTheme.colors.textFieldPlaceholder,
                )
            }
        },
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
        minLines = minLines,
        maxLines = maxLines ?: if (singleLine) 1 else Int.MAX_VALUE,
        colors = OutlinedTextFieldDefaults.colors(
            //focusedBorderColor = AppTheme.colors.primary,
            //unfocusedBorderColor = AppTheme.colors.tileBackground,
            cursorColor = AppTheme.colors.primary,
            //focusedContainerColor = AppTheme.colors.tileBackground,
            //unfocusedContainerColor = AppTheme.colors.tileBackground,
        )
    )
}

@Composable
@Preview(showBackground = true)
fun DefaultTextFieldPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "Data",
                onValueChange = {},
                label = "Test",
            )
        }
    }
}