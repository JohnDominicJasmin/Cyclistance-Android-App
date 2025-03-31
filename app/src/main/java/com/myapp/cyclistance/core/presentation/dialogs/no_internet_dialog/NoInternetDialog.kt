package com.myapp.cyclistance.core.presentation.dialogs.no_internet_dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.domain.model.DialogModel
import com.myapp.cyclistance.core.presentation.dialogs.common.PermissionDialogCreator
import com.myapp.cyclistance.core.utils.contexts.openWifiSettings
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun NoInternetDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit) {

    val context = LocalContext.current

    PermissionDialogCreator(
        modifier = modifier,
        dialogModel = DialogModel(
            icon = R.drawable.ic_no_internet_connection,
            iconContentDescription = "No Internet",
            title = "No Internet Connection",
            description = "No Internet Connection. Make sure Wi-Fi \n" +
                          "or mobile data is turned on, then try again.",
            firstButtonText = "Go to Settings",
            secondButtonText = "Okay"
        ), onDismiss = onDismiss,
        onClickGoToSettings = {
            context.openWifiSettings()
        })

}

@Preview(device = "id:Galaxy Nexus", name = "No Internet Dialog Dark Theme")
@Composable
fun PreviewNoInternetDark() {
    CyclistanceTheme(true) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
            NoInternetDialog(onDismiss = {})
        }
    }
}


@Preview(device = "id:Galaxy Nexus", name = "No Internet Dialog Light Theme")
@Composable
fun PreviewNoInternetLight() {
    CyclistanceTheme(false) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
            NoInternetDialog(onDismiss = {})
        }
    }
}

