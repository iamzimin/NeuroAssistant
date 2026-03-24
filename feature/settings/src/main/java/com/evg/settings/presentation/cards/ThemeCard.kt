package com.evg.settings.presentation.cards

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.evg.resource.R
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ThemeCard(
    selectedThemeMode: Int,
    onThemeSelected: (Int) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
        color = AppTheme.colors.tileBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        ) {
            Text(
                text = stringResource(R.string.profile_theme),
                style = AppTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                color = AppTheme.colors.text,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
            ) {
                ThemeOption(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.profile_theme_light),
                    selected = selectedThemeMode == AppCompatDelegate.MODE_NIGHT_NO,
                    onClick = { onThemeSelected(AppCompatDelegate.MODE_NIGHT_NO) },
                )
                ThemeOption(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.profile_theme_dark),
                    selected = selectedThemeMode == AppCompatDelegate.MODE_NIGHT_YES,
                    onClick = { onThemeSelected(AppCompatDelegate.MODE_NIGHT_YES) },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ThemeCardPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ThemeCard(
                selectedThemeMode = AppTheme.nightMode,
                onThemeSelected = {},
            )
        }
    }
}