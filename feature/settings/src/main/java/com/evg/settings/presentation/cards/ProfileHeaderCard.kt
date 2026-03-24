package com.evg.settings.presentation.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.evg.resource.R
import com.evg.settings.presentation.mvi.SettingsState
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ProfileHeaderCard(
    state: SettingsState,
    onChangePhotoClick: () -> Unit,
) {
    val imageModel = state.selectedPhotoBytes ?: state.profile?.photoBytes ?: state.profile?.photoUrl

    Surface(
        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
        color = AppTheme.colors.tileBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        ) {
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.background)
                    .clickable(onClick = onChangePhotoClick),
                contentAlignment = Alignment.Center,
            ) {
                if (imageModel == null) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(28.dp),
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(R.string.profile_photo),
                        colorFilter = ColorFilter.tint(AppTheme.colors.primary),
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = imageModel,
                        contentDescription = stringResource(R.string.profile_photo),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Text(
                text = stringResource(R.string.profile_change_photo),
                style = AppTheme.typography.body,
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileHeaderCardPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ProfileHeaderCard(
                state = SettingsState(),
                onChangePhotoClick = {},
            )
        }
    }
}