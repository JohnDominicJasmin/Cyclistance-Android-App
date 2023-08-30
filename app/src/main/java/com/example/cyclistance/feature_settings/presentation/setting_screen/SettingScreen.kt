package com.example.cyclistance.feature_settings.presentation.setting_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_settings.presentation.setting_screen.components.SettingScreenContent
import com.example.cyclistance.feature_settings.presentation.setting_screen.event.SettingUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.state.SettingUiState
import com.example.cyclistance.navigation.Screens

@Composable
fun SettingScreen(
    onToggleTheme: () -> Unit,
    paddingValues: PaddingValues,
    navController: NavController) {


    var uiState by rememberSaveable{ mutableStateOf(SettingUiState())}
    val onClickEditProfile = remember{{
        navController.navigate(Screens.UserProfileNavigation.EditProfile.screenRoute)
    }}

    val onClickResetPassword = remember{{
        navController.navigate(Screens.AuthenticationNavigation.ResetPassword.screenRoute)
    }}

    val setUrlToOpen = remember{{ urlToOpen: String? ->
        uiState = uiState.copy(urlToOpen = urlToOpen)
    }}

    SettingScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        uiState = uiState,
        event = { event ->
            when(event){
                SettingUiEvent.ClickResetPassword -> onClickResetPassword()
                SettingUiEvent.ClickEditProfile -> onClickEditProfile()
                SettingUiEvent.ClickToggleTheme -> onToggleTheme()
                is SettingUiEvent.OpenWebView -> setUrlToOpen(event.url)
                SettingUiEvent.DismissWebView -> setUrlToOpen(null)
            }
        }

    )

}
