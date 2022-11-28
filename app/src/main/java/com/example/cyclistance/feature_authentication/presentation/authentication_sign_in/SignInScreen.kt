package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
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
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.domain.util.AuthResult
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthState
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.*
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    hasInternetConnection: Boolean,
    signInViewModel: SignInViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val scope = rememberCoroutineScope()
    val signInState by signInViewModel.state.collectAsState()
    val emailAuthState by emailAuthViewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResult()) { task ->
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            account?.let {
                scope.launch {
                    signInViewModel.onEvent(event = SignInEvent.SignInGoogle( authCredential = GoogleAuthProvider.getCredential(account.idToken, null)))
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
                is SignInUiEvent.ShowMappingScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }
                is SignInUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, signInEvent.message, Toast.LENGTH_SHORT).show()
                }


            }
        }
    }


    LaunchedEffect(key1 = true) {
        emailAuthViewModel.eventFlow.collectLatest { emailAuthEvent ->
            when (emailAuthEvent) {
                is EmailAuthUiEvent.ShowMappingScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingScreen.route,
                        Screens.SignInScreen.route)
                }
                is EmailAuthUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, emailAuthEvent.message, Toast.LENGTH_SHORT).show()
                }
                is EmailAuthUiEvent.ShowEmailAuthScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignInScreen.route)
                }
            }
        }
    }


    val onDismissAlertDialog = remember{{
        signInViewModel.onEvent(SignInEvent.DismissAlertDialog)
    }}
    val onDoneKeyboardAction = remember<KeyboardActionScope.() -> Unit>{{
        signInViewModel.onEvent(SignInEvent.SignInDefault)
        focusManager.clearFocus()
    }}
    val onValueChangeEmail = remember<(String) -> Unit>{{
        signInViewModel.onEvent(event = SignInEvent.EnterEmail(it))
    }}
    val onValueChangePassword = remember<(String) -> Unit>{{
        signInViewModel.onEvent(event = SignInEvent.EnterPassword(it))
    }}
    val onClickPasswordVisibility = remember {{
        signInViewModel.onEvent(SignInEvent.TogglePasswordVisibility)
    }}
    val onClickFacebookButton = remember {{
        signInViewModel.onEvent(SignInEvent.SignInFacebook(activity = context.findActivity()))
    }}
    val onClickGoogleButton = remember{{
        scope.launch {
            authResultLauncher.launch(GOOGLE_SIGN_IN_REQUEST_CODE)
        }
        Unit
    }}
    val onClickSignInButton = remember{{
        signInViewModel.onEvent(SignInEvent.SignInDefault)
    }}
    val onClickSignInText = remember {{
        navController.navigateScreen(Screens.SignUpScreen.route)
    } }



    SignInScreen(
        modifier = Modifier.padding(paddingValues),
        signInState = signInState,
        emailAuthState = emailAuthState,
        focusRequester = focusRequester,
        onDismissAlertDialog = onDismissAlertDialog,
        keyboardActionOnDone = onDoneKeyboardAction,
        onValueChangeEmail = onValueChangeEmail,
        onValueChangePassword = onValueChangePassword,
        onClickPasswordVisibility = onClickPasswordVisibility,
        onClickFacebookButton = onClickFacebookButton,
        onClickGoogleButton = onClickGoogleButton,
        onClickSignInButton = onClickSignInButton,
        onClickSignInText = onClickSignInText,
    )
}

@Preview
@Composable
fun SignInScreenPreview() {
    CyclistanceTheme(true) {
        SignInScreen()
    }
}


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    signInState: SignInState = SignInState(),
    emailAuthState: EmailAuthState = EmailAuthState(),
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


    ConstraintLayout(
        constraintSet = signInConstraints,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {

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

        if (signInState.alertDialogModel.visible()) {
            AlertDialog(
                alertDialog = signInState.alertDialogModel,
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
            onClickPasswordVisibility = onClickPasswordVisibility
        )

        val isLoading = remember(signInState.isLoading, emailAuthState.isLoading) {
            (signInState.isLoading || emailAuthState.isLoading)
        }

        val hasInternet = remember(emailAuthState.hasInternet, signInState.hasInternet){
            emailAuthState.hasInternet || signInState.hasInternet
        }

        SignInGoogleAndFacebookSection(
            onClickFacebookButton = onClickFacebookButton,
            onClickGoogleButton = onClickGoogleButton,
            enabled = !isLoading
        )

        SignInButton(onClickSignInButton = onClickSignInButton, enabled = !isLoading )

        SignInClickableText(onClickSignInText = onClickSignInText, enabled = !isLoading)

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
            )
        }

        if (!hasInternet) {
            NoInternetDialog(
                onDismiss = onDismissNoInternetDialog,
            modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId))

        }


    }
}





