package com.evg.settings.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.evg.resource.R
import com.evg.settings.presentation.model.SelectedImage
import com.evg.settings.presentation.mvi.SettingsAction
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.evg.settings.presentation.mvi.SettingsSideEffect
import com.evg.settings.presentation.mvi.SettingsViewModel
import com.evg.ui.mapper.toErrorMessage
import com.evg.ui.snackbar.SnackBarAction
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.snackbar.SnackBarEvent
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsRoot(
    modifier: Modifier,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

        scope.launch {
            val selectedImage = withContext(Dispatchers.IO) {
                extractSelectedImage(
                    context = context,
                    intent = result.data,
                )
            } ?: return@launch

            viewModel.dispatch(
                SettingsAction.OnPhotoSelected(
                    bytes = selectedImage.bytes,
                    fileExtension = selectedImage.fileExtension,
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dispatch(SettingsAction.Initialize)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SettingsSideEffect.ShowBalanceError -> {
                SnackBarController.sendEvent(
                    SnackBarEvent(
                        message = sideEffect.error.toErrorMessage(context),
                        action = SnackBarAction(
                            name = context.getString(R.string.retry),
                            action = {
                                viewModel.dispatch(SettingsAction.OnRetryBalanceClicked)
                            },
                        ),
                    )
                )
            }

            is SettingsSideEffect.ShowProfileUpdateError -> {
                SnackBarController.sendEvent(
                    SnackBarEvent(
                        message = sideEffect.error.toErrorMessage(context),
                        action = SnackBarAction(
                            name = context.getString(R.string.retry),
                            action = {
                                viewModel.dispatch(SettingsAction.OnRetryProfileUpdateClicked)
                            },
                        ),
                    )
                )
            }

            SettingsSideEffect.NavigateToAuth -> onLogout()
        }
    }

    SettingsScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        dispatch = viewModel::dispatch,
        onChangePhotoClick = {
            viewModel.dispatch(SettingsAction.OnEditClicked)
            imagePickerLauncher.launch(createImagePickerIntent())
        },
    )
}

private fun createImagePickerIntent(): Intent {
    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    return Intent.createChooser(galleryIntent, null).apply {
        putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
    }
}

private fun extractSelectedImage(
    context: Context,
    intent: Intent?,
): SelectedImage? {
    intent ?: return null

    intent.data?.let { uri ->
        val bytes = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: return null

        val mimeType = context.contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            ?: MimeTypeMap.getFileExtensionFromUrl(uri.toString())

        return SelectedImage(
            bytes = bytes,
            fileExtension = extension,
        )
    }

    val bitmap = intent.extras?.get("data") as? Bitmap ?: return null
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)

    return SelectedImage(
        bytes = outputStream.toByteArray(),
        fileExtension = "jpg",
    )
}