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
fun DialogFilesAndMediaPermission(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {


    val context = LocalContext.current

    PermissionDialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_files_and_media_permission,
            iconContentDescription = "Files and media permission",
            title = "Enable Files and Media Permission",
            description = "We kindly request your permission to access your device's files and media for you to upload photos.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Not now"
        ), onDismiss = onDismiss,
        onClickGoToSettings = context::openAppSettings
    )

}

@Preview(name = "DialogFilesAndMediaPermissionDark", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogFilesAndMediaPermissionDark() {

    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogFilesAndMediaPermission(onDismiss = {})
        }
    }
}

@Preview(name = "DialogFilesAndMediaPermissionLight", device = "id:Galaxy Nexus")
@Composable
private fun PreviewDialogFilesAndMediaPermissionLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DialogFilesAndMediaPermission(onDismiss = {})
        }
    }
}