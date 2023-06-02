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
fun DialogPhonePermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    DialogCreator(
        dialogModel = DialogModel(
            icon = R.drawable.ic_phone_permission,
            iconContentDescription = "Phone permission",
            title = "Enable Phone Permission",
            description = "We need your permission to send you notifications so you can receive updates and reminders at the right time.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ),
        onDismiss = onDismiss,
        modifier = modifier,
        onClickGoToSettings = context::openAppSettings)
}

@Preview(name = "DialogPhonePermissionDark", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogPhonePermissionDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogPhonePermission(onDismiss = {})
        }
    }
}

@Preview(name = "DialogPhonePermissionLight", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogPhonePermissionLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogPhonePermission(onDismiss = {})
        }
    }
}