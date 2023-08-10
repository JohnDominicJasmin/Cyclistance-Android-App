package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.components.ForgotPasswordContent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordUiState

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = viewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by rememberSaveable {
        mutableStateOf(ForgotPasswordUiState())
    }
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val clearEmail = remember {
        {
            email = TextFieldValue()
        }
    }

    val onChangeEmail = remember {
        { input: TextFieldValue ->
            email = input
        }
    }

    val onClickCancel = remember{{
        navController.popBackStack()
    }}


    ForgotPasswordContent(state = state, modifier = Modifier.padding(paddingValues),
        email = email,
        uiState = uiState,
        event = { event ->
            when (event) {
                ForgotPasswordUiEvent.ClearEmailInput -> clearEmail()
                is ForgotPasswordUiEvent.OnChangeEmail -> onChangeEmail(event.email)
                ForgotPasswordUiEvent.OnClickCancel -> onClickCancel()
                ForgotPasswordUiEvent.OnClickSubmit -> TODO()
                ForgotPasswordUiEvent.DismissAlertDialog -> TODO()
            }
        }
    )

}