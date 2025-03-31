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
fun DialogPhonePermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    PermissionDialogCreator(
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