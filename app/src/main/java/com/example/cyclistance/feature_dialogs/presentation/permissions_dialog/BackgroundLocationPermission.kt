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
fun DialogBackgroundLocationPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current

    DialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_background_location_permission,
            iconContentDescription = "Enable Background Location Permission",
            title = "Enable Background Location Permission",
            description = "Ensures that the app can deliver uninterrupted location-based services, guaranteeing timely notifications, even when the app is in the background or not actively in use.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ),
        onDismiss = onDismiss,
        onClickGoToSettings = context::openAppSettings)

}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewDialogBackgroundLocationPermissionDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogBackgroundLocationPermission(onDismiss = {})
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewDialogBackgroundLocationPermissionLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogBackgroundLocationPermission(onDismiss = {})
        }
    }
}


