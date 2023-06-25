package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.AuthConstants.GOOGLE_SIGN_IN_REQUEST_CODE
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.util.AuthResult
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_email.event.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.event.EmailAuthVmEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreenContent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event.SignInEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event.SignInVmEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event.SignUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.state.SignInUiState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val signInState by signInViewModel.state.collectAsStateWithLifecycle()
    val emailAuthState by emailAuthViewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    var uiState by rememberSaveable {
        mutableStateOf(SignInUiState())
    }

    val focusManager = LocalFocusManager.current
    val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResult()) { task ->
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                scope.launch {
                    signInViewModel.onEvent(
                        event = SignInVmEvent.SignInGoogle(
                            authCredential = SignInCredential.Google(
                                providerToken = token)))
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    val callbackManager = LocalActivityResultCallbackManager.current
    DisposableEffect(Unit) {
        callbackManager.addListener(signInViewModel)
        onDispose {
            callbackManager.removeListener(signInViewModel)
        }
    }



    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        signInViewModel.eventFlow.collectLatest { signInEvent ->

            when (signInEvent) {

                is SignInEvent.RefreshEmail -> {
                    emailAuthViewModel.onEvent(EmailAuthVmEvent.RefreshEmail)
                }

                is SignInEvent.SignInSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }

                is SignInEvent.SignInFailed -> {
                    Toast.makeText(context, signInEvent.reason, Toast.LENGTH_SHORT).show()
                }

                is SignInEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetVisible = true
                    )
                }

                is SignInEvent.AccountBlockedTemporarily -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Account Blocked Temporarily",
                            description = "You have been blocked temporarily for too many failed attempts. Please try again later.",
                            icon = R.raw.error,
                        )
                    )
                }

                is SignInEvent.ConflictFbToken -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Conflict Facebook Account",
                            description = "Sorry, something went wrong. Please try again.",
                            icon = R.raw.error)
                    )
                }

                is SignInEvent.FacebookSignInFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Facebook Sign In Failed",
                            description = "Failed to sign in with Facebook. Please try again.",
                            icon = R.raw.error)
                    )
                }

                is SignInEvent.InvalidEmail -> {
                    uiState = uiState.copy(
                        emailErrorMessage = signInEvent.reason
                    )
                }

                is SignInEvent.InvalidPassword -> {
                    uiState = uiState.copy(
                        passwordErrorMessage = signInEvent.reason
                    )
                }

            }
        }
    }


    LaunchedEffect(key1 = true) {
        emailAuthViewModel.eventFlow.collectLatest { emailAuthEvent ->
            when (emailAuthEvent) {
                is EmailAuthEvent.EmailVerificationSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }

                is EmailAuthEvent.ReloadEmailFailed -> {
                    Toast.makeText(context, emailAuthEvent.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthEvent.EmailVerificationNotSent -> {
                    Toast.makeText(context, emailAuthEvent.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthEvent.EmailVerificationFailed -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignInScreen.route)
                }

                is EmailAuthEvent.EmailVerificationSent -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "New Email Sent.",
                            description = "New verification email has been sent to your email address.",
                        )
                    )
                }

                is EmailAuthEvent.SendEmailVerificationFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Email Verification Failed.",
                            description = "Failed to send verification email. Please try again later.",
                            icon = R.raw.error)
                    )
                }

                else -> {}
            }
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
            signInViewModel.onEvent(
                SignInVmEvent.SignInWithEmailAndPassword(
                    email = uiState.email.text,
                    password = uiState.password.text))
            focusManager.clearFocus()
        }
    }

    val onValueChangeEmail = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                email = it,
                emailErrorMessage = ""
            )
        }
    }

    val onValueChangePassword = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                password = it,
                passwordErrorMessage = ""
            )
        }
    }

    val onClickPasswordVisibility = remember {
        {
            uiState = uiState.copy(
                isPasswordVisible = !uiState.isPasswordVisible
            )
        }
    }

    val onClickFacebookButton = remember {
        {
            signInViewModel.onEvent(SignInVmEvent.SignInFacebook(activity = context.findActivity()))
        }
    }

    val onClickGoogleButton = remember {
        {
            scope.launch {
                authResultLauncher.launch(GOOGLE_SIGN_IN_REQUEST_CODE)
            }
            Unit
        }
    }

    val onClickSignInButton = remember {
        {



            signInViewModel.onEvent(
                SignInVmEvent.SignInWithEmailAndPassword(
                    email = uiState.email.text,
                    password = uiState.password.text
                ))
        }
    }

    val onClickSignInText = remember {
        {
            navController.navigateScreen(Screens.SignUpScreen.route)
        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetVisible = false
            )
        }
    }

    SignInScreenContent(
        modifier = Modifier.padding(paddingValues),
        signInState = signInState,
        emailAuthState = emailAuthState,
        focusRequester = focusRequester,
        uiState = uiState,
        event = { event ->
            when (event) {
                is SignUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is SignUiEvent.KeyboardActionDone -> onDoneKeyboardAction()
                is SignUiEvent.OnChangeEmail -> onValueChangeEmail(event.email)
                is SignUiEvent.OnChangePassword -> onValueChangePassword(event.password)
                is SignUiEvent.TogglePasswordVisibility -> onClickPasswordVisibility()
                is SignUiEvent.SignInWithFacebook -> onClickFacebookButton()
                is SignUiEvent.SignInWithGoogle -> onClickGoogleButton()
                is SignUiEvent.SignInWithEmailAndPassword -> onClickSignInButton()
                is SignUiEvent.NavigateToSignUp -> onClickSignInText()
                is SignUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
            }
        }
    )
}
