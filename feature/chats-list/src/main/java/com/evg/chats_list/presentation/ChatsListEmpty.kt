package com.evg.chats_list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.evg.resource.R
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ChatsListEmpty(
    modifier: Modifier = Modifier,
    hasActiveSearch: Boolean,
) {
    val titleRes = if (hasActiveSearch) {
        R.string.empty_search_title
    } else {
        R.string.empty_chats_title
    }
    val subtitleRes = if (hasActiveSearch) {
        R.string.empty_search_subtitle
    } else {
        R.string.empty_chats_subtitle
    }

    Column(
        modifier = modifier.padding(AppTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(titleRes),
            style = AppTheme.typography.heading,
            color = AppTheme.colors.text,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

        Text(
            text = stringResource(subtitleRes),
            style = AppTheme.typography.body,
            color = AppTheme.colors.text.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatsListEmptyPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatsListEmpty(
                modifier = Modifier,
                hasActiveSearch = false,
            )
        }
    }
}