package com.evg.login.presentation

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.resource.R
import com.evg.ui.extensions.clickableRipple
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun SocialLoginButton(
    icon: Painter,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.dimens.borderRadius)
    val clickModifier = if (enabled) {
        Modifier.clickableRipple(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                color = AppTheme.colors.textFieldPlaceholder,
                shape = shape,
            )
            .alpha(if (enabled) 1f else 0.5f)
            .then(clickModifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            painter = icon,
            contentDescription = contentDescription,
            tint = Color.Unspecified,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SocialLoginButtonPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            SocialLoginButton(
                icon = painterResource(id = R.drawable.google),
                contentDescription = "googleSignInText",
                enabled = true,
                onClick = {},
            )
        }
    }
}