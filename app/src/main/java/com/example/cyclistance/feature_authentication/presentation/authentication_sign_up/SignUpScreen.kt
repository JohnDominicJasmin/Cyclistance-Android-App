package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreenContent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpVmEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state.SignUpUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreenInclusively
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val signUpState by signUpViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }


    var uiState by rememberSaveable { mutableStateOf(SignUpUiState()) }

    val signUpAccount = remember(key1 = email, key2 = signUpState.hasAccountSignedIn) {
        {
            val isUserCreatedNewAccount = email.text != signUpState.savedAccountEmail
            if (signUpState.hasAccountSignedIn && isUserCreatedNewAccount) {
                signUpViewModel.onEvent(SignUpVmEvent.SignOut)
            }
            signUpViewModel.onEvent(
                SignUpVmEvent.SignUp(
                    email = email.text,
                    password = password.text,
                    confirmPassword = confirmPassword.text))
        }

    }

    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val onDoneKeyboardAction = remember {
        {
            signUpAccount()
            focusManager.clearFocus()
        }
    }
    val onValueChangeEmail = remember {
        { inputEmail: TextFieldValue ->
            uiState = uiState.copy(
                emailErrorMessage = ""
            )
            email = inputEmail
        }
    }
    val onValueChangePassword = remember {
        { inputPassword: TextFieldValue ->
            uiState = uiState.copy(
                passwordErrorMessage = ""
            )
            password = inputPassword
        }
    }
    val onValueChangeConfirmPassword = remember {
        { inputConfirmPassword: TextFieldValue ->
            uiState = uiState.copy(
                confirmPasswordErrorMessage = ""
            )
            confirmPassword = inputConfirmPassword
        }
    }
    val onClickPasswordVisibility = remember {
        {
            uiState = uiState.copy(
                passwordVisible = !uiState.passwordVisible
            )
        }
    }
    val onClickSignUpButton = remember {
        {
            signUpAccount()
        }
    }
    val onClickSignUpText = remember {
        {
            navController.navigateScreenInclusively(
                Screens.Authentication.SignInScreen.screenRoute,
                Screens.Authentication.SignUpScreen.screenRoute)
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetVisible = false
            )
        }
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        signUpViewModel.eventFlow.collectLatest { event ->

            when (event) {
                is SignUpEvent.SignUpSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.Authentication.EmailAuthScreen.screenRoute,
                        Screens.Authentication.SignUpScreen.screenRoute)

                }

                is SignUpEvent.CreateAccountFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Failed to create account",
                            description = " Failed to create account. Check info and try again or contact support.",
                            icon = R.raw.error
                        )
                    )

                }

                is SignUpEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetVisible = true
                    )
                }

                is SignUpEvent.AccountAlreadyTaken -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Account already taken",
                            description = "Account already taken. Try again or contact support.",
                            icon = R.raw.error
                        )
                    )
                }

                is SignUpEvent.InvalidEmail -> {
                    uiState = uiState.copy(
                        emailErrorMessage = event.reason
                    )
                }

                is SignUpEvent.InvalidPassword -> {
                    uiState = uiState.copy(
                        passwordErrorMessage = event.reason
                    )
                }

                is SignUpEvent.InvalidConfirmPassword -> {
                    uiState = uiState.copy(
                        confirmPasswordErrorMessage = event.reason
                    )
                }
            }
        }
    }




    SignUpScreenContent(
        modifier = Modifier.padding(paddingValues),
        focusRequester = focusRequester,
        signUpState = signUpState,
        uiState = uiState,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        event = { event ->
            when (event) {
                is SignUpUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is SignUpUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is SignUpUiEvent.KeyboardActionDone -> onDoneKeyboardAction()
                is SignUpUiEvent.OnChangeEmail -> onValueChangeEmail(event.email)
                is SignUpUiEvent.OnChangePassword -> onValueChangePassword(event.password)
                is SignUpUiEvent.OnChangeConfirmPassword -> onValueChangeConfirmPassword(event.confirmPassword)
                is SignUpUiEvent.TogglePasswordVisibility -> onClickPasswordVisibility()
                is SignUpUiEvent.SignUpWithEmailAndPassword -> onClickSignUpButton()
                is SignUpUiEvent.NavigateToSignIn -> onClickSignUpText()
            }
        }
    )
}

