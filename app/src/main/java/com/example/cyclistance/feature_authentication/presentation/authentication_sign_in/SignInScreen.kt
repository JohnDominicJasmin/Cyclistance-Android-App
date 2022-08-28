package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.AuthConstants.GOOGLE_SIGN_IN_REQUEST_CODE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.SetupAlertDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthUiEvent

import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.domain.util.AuthResult
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    onBackPressed:() -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

    val scope = rememberCoroutineScope()
    val signInStateValue = signInViewModel.state
    val emailAuthStateValue = emailAuthViewModel.state

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    BackHandler(enabled = true, onBack = onBackPressed)
    val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResult()) { task ->
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            account?.let{
                scope.launch {
                    signInViewModel.onEvent(event = SignInEvent.SignInGoogle(authCredential = GoogleAuthProvider.getCredential(account.idToken, null)))
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
        }
    }

    val callbackManager = LocalActivityResultCallbackManager.current
    DisposableEffect(Unit) {
        callbackManager.addListener(signInViewModel)
        onDispose {
            callbackManager.removeListener(signInViewModel)
        }
    }






            var alertDialogState by remember { mutableStateOf(AlertDialogModel()) }

            LaunchedEffect(key1 = true) {
                signInStateValue.focusRequester.requestFocus()
                signInViewModel.eventFlow.collectLatest { signInEvent ->

                    when (signInEvent) {

                        is SignInUiEvent.RefreshEmail -> {
                            emailAuthViewModel.onEvent(EmailAuthEvent.RefreshEmail)
                        }
                        is SignInUiEvent.ShowNoInternetScreen -> {
                            navigateTo(Screens.NoInternetScreen.route, null)
                        }
                        is SignInUiEvent.ShowAlertDialog -> {
                            alertDialogState = AlertDialogModel(
                                title = signInEvent.title,
                                description = signInEvent.description,
                                icon = signInEvent.imageResId)
                        }
                        is SignInUiEvent.ShowMappingScreen -> {
                            navigateTo(Screens.MappingScreen.route, Screens.SignInScreen.route)
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
                        is EmailAuthUiEvent.ShowNoInternetScreen -> {
                            navigateTo(Screens.NoInternetScreen.route, null)
                        }

                        is EmailAuthUiEvent.ShowMappingScreen -> {
                            navigateTo(Screens.MappingScreen.route, Screens.SignInScreen.route)
                        }
                        is EmailAuthUiEvent.ShowToastMessage -> {
                            Toast.makeText(context, emailAuthEvent.message, Toast.LENGTH_SHORT).show()
                        }
                        is EmailAuthUiEvent.ShowEmailAuthScreen -> {
                              navigateTo(Screens.EmailAuthScreen.route, Screens.SignInScreen.route)
                        }
                        else -> {}
                    }
                }
            }

            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {

                ConstraintLayout(
                    constraintSet = signInConstraints,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)) {


                    Image(
                        contentDescription = "App Icon",
                        painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                        modifier = Modifier
                            .height(100.dp)
                            .width(90.dp)
                            .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)
                    )


                    SignUpTextArea()

                    if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                        SetupAlertDialog(
                            alertDialog = alertDialogState,
                            onDismissRequest = {
                                alertDialogState = AlertDialogModel()
                            })
                    }

                    Waves(
                        topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                        bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
                    )


                    SignInTextFieldsArea(
                        focusRequester = signInStateValue.focusRequester,
                        state = signInStateValue,
                        signInViewModel = signInViewModel,
                        keyboardActionOnDone = {
                            signInViewModel.onEvent(SignInEvent.SignInDefault)
                            focusManager.clearFocus()

                        })

                    SignInGoogleAndFacebookSection(
                        facebookButtonOnClick = {
                            signInViewModel.onEvent(SignInEvent.SignInFacebook(context = context))
                        },
                        googleSignInButtonOnClick = {
                            scope.launch {
                                authResultLauncher.launch(GOOGLE_SIGN_IN_REQUEST_CODE)
                            }
                        }
                    )

                    SignInButton(onClickButton = {
                        signInViewModel.onEvent(SignInEvent.SignInDefault)
                    })


                    SignInClickableText(onClick = {
                        navigateTo(Screens.SignUpScreen.route, null)
                    })

                    if (signInStateValue.isLoading || emailAuthStateValue.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                        )
                    }

                }


            }
    }

@Preview(device = Devices.NEXUS_5)
@Composable
fun SignInScreenPreview() {

    CyclistanceTheme(false) {

        val scope = rememberCoroutineScope()
        var alertDialogState by remember { mutableStateOf(AlertDialogModel()) }


        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {

            ConstraintLayout(
                constraintSet = signInConstraints,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {


                Image(
                    contentDescription = "App Icon",
                    painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                    modifier = Modifier
                        .height(100.dp)
                        .width(90.dp)
                            .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId))

                SignUpTextArea()

                if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                    SetupAlertDialog(
                        alertDialog = alertDialogState,
                        onDismissRequest = {
                            alertDialogState = AlertDialogModel()
                        })
                }

                Waves(
                    topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                    bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
                )


                SignInTextFieldsArea(
                    state = SignInState(),
                    keyboardActionOnDone = { },
                    signInViewModel = null, focusRequester = FocusRequester())

                SignInGoogleAndFacebookSection(
                    facebookButtonOnClick = {
                    },
                    googleSignInButtonOnClick = {
                        scope.launch {
                        }
                    }
                )

                SignInButton(onClickButton = {
                })


                SignInClickableText(onClick = {
                })

                if (true) {
                    CircularProgressIndicator(
                        modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                    )
                }

            }

        }
    }
}





