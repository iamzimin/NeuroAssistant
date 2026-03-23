package com.evg.chat.presentation.message

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.resource.R
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun GeneratingMessagePlaceholder() {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppTheme.colors.shimmer)
                .shimmer(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppTheme.colors.shimmer)
                .shimmer(),
        )
        Text(
            text = stringResource(R.string.chat_generating),
            style = AppTheme.typography.small,
            color = AppTheme.colors.text,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun GeneratingMessagePlaceholderPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            GeneratingMessagePlaceholder()
        }
    }
}