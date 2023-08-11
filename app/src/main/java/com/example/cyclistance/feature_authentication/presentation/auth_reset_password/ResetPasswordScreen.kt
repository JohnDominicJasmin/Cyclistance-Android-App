package com.example.cyclistance.feature_authentication.presentation.auth_reset_password

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.components.ResetPasswordContent
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state.ResetPasswordUiState

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    var uiState by rememberSaveable { mutableStateOf(ResetPasswordUiState()) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var currentPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var newPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }


    val dismissAlertDialog = remember {
        {
            uiState = uiState.copy(alertDialogState = AlertDialogState())
        }
    }

    val dismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(isNoInternetVisible = false)
        }
    }

    val onKeyboardActionDone = remember {
        {

        }
    }

    val onChangeCurrentPassword = remember {
        { input: TextFieldValue ->
            uiState = uiState.copy(currentPasswordErrorMessage = "")
            currentPassword = input
        }
    }

   val onChangeNewPassword = remember {
        { input: TextFieldValue ->
            uiState = uiState.copy(newPasswordErrorMessage = "")
            newPassword = input
        }
    }

   val onChangeConfirmPassword = remember {
        { input: TextFieldValue ->
            uiState = uiState.copy(confirmPasswordErrorMessage = "")
            confirmPassword = input
        }
    }

    val onClickCancel = remember {
        {
            navController.popBackStack()
        }
    }

    val onClickUpdate = remember {
        {

        }
    }


    ResetPasswordContent(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        currentPassword = currentPassword,
        newPassword = newPassword,
        confirmPassword = confirmPassword,
        state = state,
        event = { event ->
            when (event) {
                ResetPasswordUiEvent.DismissAlertDialog -> dismissAlertDialog()
                ResetPasswordUiEvent.DismissNoInternetDialog -> dismissNoInternetDialog()
                ResetPasswordUiEvent.KeyboardActionDone -> onKeyboardActionDone()
                is ResetPasswordUiEvent.OnChangeCurrentPassword -> onChangeCurrentPassword(event.currentPassword)
                is ResetPasswordUiEvent.OnChangeNewPassword -> onChangeNewPassword(event.newPassword)
                is ResetPasswordUiEvent.OnChangeConfirmPassword -> onChangeConfirmPassword(event.confirmPassword)
                ResetPasswordUiEvent.OnClickCancel -> onClickCancel()
                ResetPasswordUiEvent.OnClickUpdate -> onClickUpdate()
            }
        }

    )


}