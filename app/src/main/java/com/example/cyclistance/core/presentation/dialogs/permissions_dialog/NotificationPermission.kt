package com.example.cyclistance.core.presentation.dialogs.permissions_dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.DialogModel
import com.example.cyclistance.core.presentation.dialogs.common.DialogCreator
import com.example.cyclistance.core.utils.contexts.openAppSettings
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun DialogNotificationPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current


    DialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_notification_permission,
            iconContentDescription = "Notification permission",
            title = "Enable Notification Permission",
            description = "We kindly request your permission to access your device's notifications for you to receive notifications.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ),
        onDismiss = onDismiss,
        onClickGoToSettings = context::openAppSettings)
}

@Preview(name = "DialogNotificationPermissionDark", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogNotificationPermissionDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogNotificationPermission(onDismiss = {})
        }
    }
}

@Preview(name = "DialogNotificationPermissionLight", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogNotificationPermissionLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogNotificationPermission(onDismiss = {})
        }
    }
}