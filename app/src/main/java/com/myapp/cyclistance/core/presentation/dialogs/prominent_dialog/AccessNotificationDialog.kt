package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessNotificationDialog(
    modifier: Modifier = Modifier, onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {


    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_access_notification_dark else R.drawable.ic_access_notification_light
    }
    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Notification",
        description = "Cyclistance requests permission to access message notifications to receive critical rescue alerts and vital updates for your safety.",
        icon = icon,
        warningText = "Tap 'Allow' to ensure proper functionality and timely delivery of essential notifications.",
        onDeny = onDeny,
        onAllow = onAllow)

}

@Preview
@Composable
fun PreviewAccessNotificationDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessNotificationDialog(
            modifier = Modifier,
            onDismissRequest = {

            },
            onDeny = {

            },
            onAllow = {

            }
        )
    }
}