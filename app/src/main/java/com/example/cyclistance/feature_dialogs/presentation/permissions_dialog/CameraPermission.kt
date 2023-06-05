package com.example.cyclistance.feature_dialogs.presentation.permissions_dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.contexts.openAppSettings
import com.example.cyclistance.feature_dialogs.domain.model.DialogModel
import com.example.cyclistance.feature_dialogs.presentation.common.DialogCreator
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun DialogCameraPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {

    val context = LocalContext.current

    DialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_camera_permission,
            iconContentDescription = "Camera permission",
            title = "Enable Camera Permission",
            description = "We kindly request your permission to access your device's camera for you to capture photos.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ),
        onDismiss = onDismiss,
        onClickGoToSettings = context::openAppSettings)

}

@Preview(name = "DialogCameraPermission Dark Theme", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogCameraPermissionDark() {
    CyclistanceTheme(darkTheme = true) {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogCameraPermission(onDismiss = {})
        }
    }
}

@Preview(name = "DialogCameraPermission Light Theme", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogCameraPermissionLight() {
    CyclistanceTheme(darkTheme = false) {

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogCameraPermission(
                onDismiss = {})
        }
    }
}