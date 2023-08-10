package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password

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
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.components.ForgotPasswordContent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordVmEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable {
        mutableStateOf(ForgotPasswordUiState())
    }
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ForgotPasswordEvent.ForgotPasswordFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Failed to send password reset email",
                            description = event.reason,
                            icon = R.raw.error
                        ))
                }

                is ForgotPasswordEvent.ForgotPasswordSuccess -> {
                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }

                is ForgotPasswordEvent.InvalidEmail -> {
                    uiState = uiState.copy(
                        emailErrorMessage = event.reason
                    )
                }

                is ForgotPasswordEvent.NoInternetConnection -> {
                    uiState = uiState.copy(isNoInternetVisible = true)
                }
            }
        }
    }


    val onDoneKeyboardAction = remember {
        {

            viewModel.onEvent(
                event = ForgotPasswordVmEvent.SendPasswordResetEmail(
                    email = email.text.trim()
                ))
            focusManager.clearFocus()

        }
    }


    val clearEmail = remember {
        {
            email = TextFieldValue()
        }
    }

    val onChangeEmail = remember {
        { input: TextFieldValue ->
            uiState = uiState.copy(
                emailErrorMessage = ""
            )
            email = input
        }
    }

    val onClickCancel = remember {
        {
            navController.popBackStack()
        }
    }

    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(alertDialogState = AlertDialogState())
        }
    }

    val onClickSubmit = remember {
        {
            viewModel.onEvent(ForgotPasswordVmEvent.SendPasswordResetEmail(email.text.trim()))
        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(isNoInternetVisible = false)
        }
    }




    ForgotPasswordContent(state = state, modifier = Modifier.padding(paddingValues),
        email = email,
        uiState = uiState,
        event = { event ->
            when (event) {
                ForgotPasswordUiEvent.ClearEmailInput -> clearEmail()
                is ForgotPasswordUiEvent.OnChangeEmail -> onChangeEmail(event.email)
                ForgotPasswordUiEvent.OnClickCancel -> onClickCancel()
                ForgotPasswordUiEvent.OnClickSubmit -> onClickSubmit()
                ForgotPasswordUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                ForgotPasswordUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                ForgotPasswordUiEvent.KeyboardActionDone -> onDoneKeyboardAction()
            }
        }
    )

}