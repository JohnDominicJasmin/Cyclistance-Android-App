package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.sino_track

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.core.presentation.dialogs.webview_dialog.DialogWebView

@Composable
fun SinoTrackWebView(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val mUrl = "https://www.sinotrack.com/"

    DialogWebView(
        modifier = modifier,
        mUrl = mUrl,
        onDismiss = onDismiss
    )
}