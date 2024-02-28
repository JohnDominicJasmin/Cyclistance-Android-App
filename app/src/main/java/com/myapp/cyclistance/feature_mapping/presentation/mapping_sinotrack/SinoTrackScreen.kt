package com.myapp.cyclistance.feature_mapping.presentation.mapping_sinotrack

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.myapp.cyclistance.core.presentation.dialogs.webview_dialog.DialogWebView

@Composable
fun SinoTrackScreen(
    paddingValues: PaddingValues,
    navController: NavController) {

    val mUrl = "https://www.sinotrack.com/"

    DialogWebView(
        modifier = Modifier.padding(paddingValues),
        mUrl = mUrl,
        onDismiss = {
            navController.popBackStack()
        }
    )

}