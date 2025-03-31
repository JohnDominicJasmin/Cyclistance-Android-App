package com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.components.ResetPasswordContent
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordUiEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordVmEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.state.ResetPasswordUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
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

    val resetPassword = remember {
        {
            viewModel.onEvent(
                event = ResetPasswordVmEvent.ResetPassword(
                    currentPassword = currentPassword.text.trim(),
                    newPassword = newPassword.text.trim(),
                    confirmPassword = confirmPassword.text.trim()))
        }
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
            resetPassword()
            focusManager.clearFocus()
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

    val onClickUpdate = remember {
        {
            resetPassword()
            focusManager.clearFocus()
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

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {

                is ResetPasswordEvent.ConfirmPasswordFailed -> {
                    uiState = uiState.copy(confirmPasswordErrorMessage = event.reason)
                }

                is ResetPasswordEvent.CurrentPasswordFailed -> {
                    uiState = uiState.copy(currentPasswordErrorMessage = event.reason)
                }

                is ResetPasswordEvent.NewPasswordFailed -> {
                    uiState = uiState.copy(newPasswordErrorMessage = event.reason)
                }

                is ResetPasswordEvent.NoInternetConnection -> {
                    uiState = uiState.copy(isNoInternetVisible = true)
                }

                is ResetPasswordEvent.ResetPasswordFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Reset Password Failed",
                            description = event.reason,
                            icon = R.raw.error
                        ))
                }

                is ResetPasswordEvent.ResetPasswordSuccess -> {
                    Toast.makeText(context, "Reset Password Success", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
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