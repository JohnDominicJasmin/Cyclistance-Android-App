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
fun DialogForegroundLocationPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit) {

    val context = LocalContext.current

    DialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_foreground_location_permission,
            iconContentDescription = "Enable Foreground Location Permission",
            title = "Enable Foreground Location Permission",
            description = " Allows the app to access your device's location while it is in the foreground, ensuring accurate and relevant information based on your current whereabouts.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ),
        onDismiss = onDismiss,
        onClickGoToSettings = context::openAppSettings)

}

@Preview(device = "id:Galaxy Nexus", name = "Location Permission Dialog Dark Theme")
@Composable
fun PreviewDialogLocationPermissionDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogForegroundLocationPermission(onDismiss = {})
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "Location Permission Dialog Light Theme")
@Composable
fun PreviewDialogLocationPermissionLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogForegroundLocationPermission(onDismiss = {})
        }
    }
}