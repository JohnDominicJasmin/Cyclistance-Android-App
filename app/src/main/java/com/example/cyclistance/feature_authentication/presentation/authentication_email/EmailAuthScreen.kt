package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.ConnectionStatus
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthResendButton
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthTextStatus
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthVerifyEmailButton
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.emailAuthConstraints
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun EmailAuthScreen(
    isDarkTheme: Boolean = false,
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {


    val context = LocalContext.current

    val emailAuthState by emailAuthViewModel.state.collectAsState()
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }





    LaunchedEffect(key1 = true) {

        with(emailAuthViewModel) {
            onEvent(EmailAuthEvent.StartTimer)
            onEvent(EmailAuthEvent.SendEmailVerification)
            onEvent(EmailAuthEvent.SubscribeEmailVerification)

            eventFlow.collectLatest { event ->

                when (event) {

                    is EmailAuthUiEvent.ShowMappingScreen -> {
                        navController.navigateScreenInclusively(
                            Screens.MappingScreen.route,
                            Screens.EmailAuthScreen.route)
                    }
                    is EmailAuthUiEvent.ShowToastMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Timber.d("User email is not verified yet. Verification is not success.")
                    }
                }
            }
        }

    }





    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colors.background)) {


        ConstraintLayout(
            constraintSet = emailAuthConstraints,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(MaterialTheme.colors.background)) {

            Image(
                contentDescription = "App Icon",
                painter = painterResource(id = if (isDarkTheme) R.drawable.ic_dark_email else R.drawable.ic_light_email),
                modifier = Modifier
                    .height(165.dp)
                    .width(155.dp)
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)

            )

            EmailAuthTextStatus(email = emailAuthState.savedAccountEmail)

            if (emailAuthState.alertDialogModel.run { title.isNotEmpty() || description.isNotEmpty() }) {
                AlertDialog(
                    alertDialog = emailAuthState.alertDialogModel,
                    onDismissRequest = {
                        emailAuthViewModel.onEvent(EmailAuthEvent.DismissAlertDialog)
                    }
                )
            }

            if (emailAuthState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(
                        AuthenticationConstraintsItem.ProgressBar.layoutId))
            }


            val secondsRemaining =
                if (emailAuthState.secondsLeft < 2) "$emailAuthState.secondsLeft" else "$emailAuthState.secondsLeft s"
            val secondsRemainingText =
                if (emailAuthState.isTimerRunning) "Resend Email in $secondsRemaining" else "Resend Email"
            EmailAuthVerifyEmailButton(onClickVerifyButton = {
                kotlin.runCatching {
                    startActivity(context, intent, null)
                }.onFailure {
                    Toast.makeText(context, "No email app detected.", Toast.LENGTH_LONG).show()
                }
            })

            EmailAuthResendButton(
                text = secondsRemainingText,
                isEnabled = !emailAuthState.isTimerRunning,
                onClickResendButton = {
                    emailAuthViewModel.apply {
                        onEvent(EmailAuthEvent.StartTimer)
                        onEvent(EmailAuthEvent.ResendButtonClick)
                        onEvent(EmailAuthEvent.SendEmailVerification)
                    }
                })

            if (!emailAuthState.hasInternet) {
                NoInternetScreen(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
                    onClickRetryButton = {
                        if (ConnectionStatus.hasInternetConnection(context)) {
                            emailAuthViewModel.onEvent(event = EmailAuthEvent.DismissNoInternetScreen)
                        }
                    })
            }

        }


    }

    EmailAuthScreen(
        modifier = Modifier.padding(paddingValues),
        emailAuthState = emailAuthState,
        isDarkTheme = isDarkTheme,
        onDismissAlertDialog = {
            emailAuthViewModel.onEvent(EmailAuthEvent.DismissAlertDialog)
        },
        onClickVerifyButton = {
            runCatching {
                startActivity(context, intent, null)
            }.onFailure {
                Toast.makeText(context, "No email app detected.", Toast.LENGTH_LONG).show()
            }
        },
        onClickResendButton = {
            emailAuthViewModel.apply {
                onEvent(EmailAuthEvent.StartTimer)
                onEvent(EmailAuthEvent.ResendButtonClick)
                onEvent(EmailAuthEvent.SendEmailVerification)
            }
        },
        onClickRetryButton = {
            if (ConnectionStatus.hasInternetConnection(context)) {
                emailAuthViewModel.onEvent(event = EmailAuthEvent.DismissNoInternetScreen)
            }
        })

}

@Preview()
@Composable
fun EmailAuthScreenPreview() {
    CyclistanceTheme(true) {
        EmailAuthScreen(isDarkTheme = true)
    }

}


@Composable
fun EmailAuthScreen(
    modifier: Modifier = Modifier,
    emailAuthState: EmailAuthState = EmailAuthState(),
    isDarkTheme: Boolean,
    onDismissAlertDialog: () -> Unit = {},
    onClickVerifyButton: () -> Unit = {},
    onClickResendButton: () -> Unit = {},
    onClickRetryButton: () -> Unit = {},
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {


        ConstraintLayout(
            constraintSet = emailAuthConstraints,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(MaterialTheme.colors.background)) {

            Image(
                contentDescription = "App Icon",
                painter = painterResource(id = if (isDarkTheme) R.drawable.ic_dark_email else R.drawable.ic_light_email),
                modifier = Modifier
                    .height(165.dp)
                    .width(155.dp)
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)

            )

            EmailAuthTextStatus(email = emailAuthState.savedAccountEmail)

            if (emailAuthState.alertDialogModel.run { title.isNotEmpty() || description.isNotEmpty() }) {
                AlertDialog(
                    alertDialog = emailAuthState.alertDialogModel,
                    onDismissRequest = onDismissAlertDialog
                )
            }

            if (emailAuthState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(
                        AuthenticationConstraintsItem.ProgressBar.layoutId))
            }


            val secondsRemaining =
                if (emailAuthState.secondsLeft < 2) "$emailAuthState.secondsLeft" else "$emailAuthState.secondsLeft s"
            val secondsRemainingText =
                if (emailAuthState.isTimerRunning) "Resend Email in $secondsRemaining" else "Resend Email"
            EmailAuthVerifyEmailButton(onClickVerifyButton = onClickVerifyButton)

            EmailAuthResendButton(
                text = secondsRemainingText,
                isEnabled = !emailAuthState.isTimerRunning,
                onClickResendButton = onClickResendButton)

            if (!emailAuthState.hasInternet) {
                NoInternetScreen(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
                    onClickRetryButton = onClickRetryButton)
            }

        }


    }
}
