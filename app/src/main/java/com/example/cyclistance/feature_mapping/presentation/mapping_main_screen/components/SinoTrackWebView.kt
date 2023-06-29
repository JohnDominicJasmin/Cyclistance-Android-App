package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SinoTrackWebView(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val mUrl = "https://www.sinotrack.com/"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )) {
        AndroidView(modifier = modifier, factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = true
                settings.setSupportZoom(true)
                settings.setGeolocationEnabled(true)
                settings.useWideViewPort = true
                settings.domStorageEnabled = true

                loadUrl(mUrl)
            }
        }, update = {

            it.loadUrl(mUrl)

        })
    }
}