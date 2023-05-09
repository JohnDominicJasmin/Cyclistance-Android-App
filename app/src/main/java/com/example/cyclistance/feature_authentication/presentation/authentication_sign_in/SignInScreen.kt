package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.AuthConstants.GOOGLE_SIGN_IN_REQUEST_CODE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.util.AuthResult
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthState
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInButton
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInClickableText
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInGoogleAndFacebookSection
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInTextFieldsArea
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignUpTextArea
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.signInConstraints
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
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

    val scope = rememberCoroutineScope()
    val signInState by signInViewModel.state.collectAsState()
    val emailAuthState by emailAuthViewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }
    var isNoInternetDialogVisible by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var emailErrorMessage by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordErrorMessage by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResult()) { task ->
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                scope.launch {
                    signInViewModel.onEvent(
                        event = SignInEvent.SignInGoogle(
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

                is SignInUiEvent.RefreshEmail -> {
                    emailAuthViewModel.onEvent(EmailAuthEvent.RefreshEmail)
                }

                is SignInUiEvent.SignInSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }

                is SignInUiEvent.SignInFailed -> {
                    Toast.makeText(context, signInEvent.reason, Toast.LENGTH_SHORT).show()
                }

                is SignInUiEvent.NoInternetConnection -> {
                    isNoInternetDialogVisible = true
                }

                is SignInUiEvent.AccountBlockedTemporarily -> {
                    alertDialogState = AlertDialogState(
                        title = "Account Blocked Temporarily",
                        description = "You have been blocked temporarily for too many failed attempts. Please try again later.",
                        icon = R.raw.error,
                    )
                }

                is SignInUiEvent.ConflictFbToken -> {
                    alertDialogState = AlertDialogState(
                        title = "Conflict Facebook Account",
                        description = "Sorry, something went wrong. Please try again.",
                        icon = R.raw.error)
                }

                is SignInUiEvent.FacebookSignInFailed -> {
                    alertDialogState = AlertDialogState(
                        title = "Facebook Sign In Failed",
                        description = "Failed to sign in with Facebook. Please try again.",
                        icon = R.raw.error)
                }

                is SignInUiEvent.InvalidEmail -> {
                    emailErrorMessage = signInEvent.reason
                }

                is SignInUiEvent.InvalidPassword -> {
                    passwordErrorMessage = signInEvent.reason
                }

            }
        }
    }


    LaunchedEffect(key1 = true) {
        emailAuthViewModel.eventFlow.collectLatest { emailAuthEvent ->
            when (emailAuthEvent) {
                is EmailAuthUiEvent.EmailVerificationSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }

                is EmailAuthUiEvent.ReloadEmailFailed -> {
                    Toast.makeText(context, emailAuthEvent.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthUiEvent.EmailVerificationNotSent -> {
                    Toast.makeText(context, emailAuthEvent.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthUiEvent.EmailVerificationFailed -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignInScreen.route)
                }

                is EmailAuthUiEvent.EmailVerificationSent -> {
                    alertDialogState = AlertDialogState(
                        title = "New Email Sent.",
                        description = "New verification email has been sent to your email address.",
                        icon = R.raw.success
                    )
                }

                is EmailAuthUiEvent.SendEmailVerificationFailed -> {
                    alertDialogState = AlertDialogState(
                        title = "Email Verification Failed.",
                        description = "Failed to send verification email. Please try again later.",
                        icon = R.raw.error)
                }

                else -> {}
            }
        }
    }


    val onDismissAlertDialog = remember {
        {
            alertDialogState = AlertDialogState()
        }
    }
    val onDoneKeyboardAction = remember<KeyboardActionScope.() -> Unit> {
        {
            signInViewModel.onEvent(
                SignInEvent.SignInWithEmailAndPassword(
                    email = email,
                    password = password))
            focusManager.clearFocus()
        }
    }

    val onValueChangeEmail = remember<(String) -> Unit> {
        {
            email = it
            emailErrorMessage = ""
        }
    }

    val onValueChangePassword = remember<(String) -> Unit> {
        {
            password = it
            passwordErrorMessage = ""
        }
    }

    val onClickPasswordVisibility = remember {
        {
            isPasswordVisible = !isPasswordVisible
        }
    }

    val onClickFacebookButton = remember {
        {
            signInViewModel.onEvent(SignInEvent.SignInFacebook(activity = context.findActivity()))
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
                SignInEvent.SignInWithEmailAndPassword(
                    email = email,
                    password = password
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
            isNoInternetDialogVisible = false
        }
    }


    SignInScreenContent(
        modifier = Modifier.padding(paddingValues),
        signInState = signInState,
        emailAuthState = emailAuthState,
        focusRequester = focusRequester,
        onDismissAlertDialog = onDismissAlertDialog,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        keyboardActionOnDone = onDoneKeyboardAction,
        onValueChangeEmail = onValueChangeEmail,
        onValueChangePassword = onValueChangePassword,
        onClickPasswordVisibility = onClickPasswordVisibility,
        onClickFacebookButton = onClickFacebookButton,
        onClickGoogleButton = onClickGoogleButton,
        onClickSignInButton = onClickSignInButton,
        onClickSignInText = onClickSignInText,
        isNoInternetDialogVisible = isNoInternetDialogVisible,
        alertDialogState = alertDialogState,
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        emailErrorMessage = emailErrorMessage,
        passwordErrorMessage = passwordErrorMessage

    )
}

@Preview
@Composable
fun SignInScreenPreview() {
    CyclistanceTheme(true) {
        SignInScreenContent(
            alertDialogState = AlertDialogState(),
            isNoInternetDialogVisible = false,
            email = "ausbdaiosbdoauwdb",
            password = "aisntqiono9iqn",
            isPasswordVisible = false,
            emailErrorMessage = "asidnaiosdnaisd",
            passwordErrorMessage = "asidnaiosdnaisd"
        )
    }
}


@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    signInState: SignInState = SignInState(),
    emailAuthState: EmailAuthState = EmailAuthState(),
    alertDialogState: AlertDialogState,
    isNoInternetDialogVisible: Boolean,
    email: String,
    emailErrorMessage: String,
    password: String,
    passwordErrorMessage: String,
    isPasswordVisible: Boolean,
    onDismissAlertDialog: () -> Unit = {},
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit) = {},
    onValueChangeEmail: (String) -> Unit = {},
    onValueChangePassword: (String) -> Unit = {},
    onClickPasswordVisibility: () -> Unit = {},
    onClickFacebookButton: () -> Unit = {},
    onClickGoogleButton: () -> Unit = {},
    onClickSignInButton: () -> Unit = {},
    onClickSignInText: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {}

) {


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        ConstraintLayout(
            constraintSet = signInConstraints,
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.layoutId(AuthenticationConstraintsItem.TopSpacer.layoutId))

            Image(
                contentDescription = "App Icon",
                painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                modifier = Modifier
                    .height(100.dp)
                    .width(90.dp)
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)
            )


            SignUpTextArea()

            if (alertDialogState.visible()) {
                AlertDialog(
                    alertDialog = alertDialogState,
                    onDismissRequest = onDismissAlertDialog)
            }

            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId,
            )

            SignInTextFieldsArea(
                focusRequester = focusRequester,
                state = signInState,
                keyboardActionOnDone = keyboardActionOnDone,
                onValueChangeEmail = onValueChangeEmail,
                onValueChangePassword = onValueChangePassword,
                onClickPasswordVisibility = onClickPasswordVisibility,
                email = email,
                password = password,
                passwordVisible = isPasswordVisible,
                emailErrorMessage = emailErrorMessage,
                passwordErrorMessage = passwordErrorMessage
            )

            val isLoading = remember(signInState.isLoading, emailAuthState.isLoading) {
                (signInState.isLoading || emailAuthState.isLoading)
            }

            SignInGoogleAndFacebookSection(
                onClickFacebookButton = onClickFacebookButton,
                onClickGoogleButton = onClickGoogleButton,
                enabled = !isLoading
            )

            SignInButton(onClickSignInButton = onClickSignInButton, enabled = !isLoading)

            SignInClickableText(onClickSignInText = onClickSignInText, enabled = !isLoading)

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                )
            }

            if (isNoInternetDialogVisible) {
                NoInternetDialog(
                    onDismiss = onDismissNoInternetDialog,
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId))

            }

        }
    }
}





