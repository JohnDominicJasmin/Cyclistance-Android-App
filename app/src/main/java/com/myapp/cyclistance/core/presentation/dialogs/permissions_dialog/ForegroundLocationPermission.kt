package com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.domain.model.DialogModel
import com.myapp.cyclistance.core.presentation.dialogs.common.PermissionDialogCreator
import com.myapp.cyclistance.core.utils.contexts.openAppSettings
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun DialogForegroundLocationPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit) {

    val context = LocalContext.current

    PermissionDialogCreator(
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