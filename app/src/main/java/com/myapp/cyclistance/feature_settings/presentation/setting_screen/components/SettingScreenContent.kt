package com.myapp.cyclistance.feature_settings.presentation.setting_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.presentation.dialogs.webview_dialog.DialogWebView
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.event.SettingUiEvent
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.state.SettingUiState
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun SettingScreenContent(
    modifier: Modifier = Modifier,
    uiState: SettingUiState,
    event: (SettingUiEvent) -> Unit,
) {

    val isDarkTheme = IsDarkTheme.current
    val context = LocalContext.current
    Surface(modifier = modifier, color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {

                    SectionTitle(iconId = R.drawable.ic_baseline_brush_24, title = "Preference")
                    SettingSwitchButton(
                        checkedState = isDarkTheme,
                        onCheckedChange = { event(SettingUiEvent.ClickToggleTheme) },
                        onClick = { event(SettingUiEvent.ClickToggleTheme) })


                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {


                    SectionTitle(iconId = R.drawable.ic_baseline_person_24, title = "Account")
                    SettingsButtonItem(
                        buttonText = "Edit Profile",
                        onClick = { event(SettingUiEvent.ClickEditProfile) })

                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {


                    SectionTitle(iconId = R.drawable.baseline_lock_24, title = "Security")
                    SettingsButtonItem(
                        buttonText = "Change Password",
                        onClick = { event(SettingUiEvent.ClickResetPassword) })

                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {

                    SectionTitle(
                        iconId = R.drawable.ic_baseline_settings_suggest_24,
                        title = "Other Settings")

                    SettingsButtonItem(buttonText = "Privacy Policy", onClick = {
                        event(SettingUiEvent.OpenWebView(context.getString(R.string.privacy_policy_url)))
                    })

                    SettingsButtonItem(buttonText = "Terms and Condition", onClick = {
                        event(SettingUiEvent.OpenWebView(context.getString(R.string.terms_and_condition_url)))
                    })

                    SettingsButtonItem(buttonText = "Rate this app", onClick = {

                    })


                }
            }


            if(uiState.urlToOpen != null){
                DialogWebView(modifier = Modifier.fillMaxSize(), mUrl = uiState.urlToOpen, onDismiss = {event(SettingUiEvent.DismissWebView)})
            }

        }
    }
}

@Preview
@Composable
fun PreviewSettingScreenDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(true) {

            SettingScreenContent(
                uiState = SettingUiState(),
                event = {}
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettingScreenLight() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(false) {

            SettingScreenContent(
                uiState = SettingUiState(),
                event = {}
            )
        }
    }
}
