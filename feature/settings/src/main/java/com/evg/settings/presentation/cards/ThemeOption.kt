package com.evg.settings.presentation.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ThemeOption(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) AppTheme.colors.primary else AppTheme.colors.text.copy(alpha = 0.2f),
        ),
        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                AppTheme.colors.primary.copy(alpha = 0.1f)
            } else {
                AppTheme.colors.background
            },
            contentColor = AppTheme.colors.text,
        ),
    ) {
        Text(text = label)
    }
}

@Composable
@Preview(showBackground = true)
private fun ThemeOptionPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ThemeOption(
                label = "test",
                selected = true,
                onClick = {},
            )
        }
    }
}