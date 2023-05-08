package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthResendButton
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthTextStatus
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthVerifyEmailButton
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.emailAuthConstraints
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@Composable
fun EmailAuthScreen(
    isDarkTheme: Boolean,
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {


    val context = LocalContext.current

    val emailAuthState by emailAuthViewModel.state.collectAsState()
    var isNoInternetDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }
    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }


    val intent = remember {
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_EMAIL)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    val onDismissAlertDialog = remember {
        {
            alertDialogState = AlertDialogState()
        }
    }

    val onClickVerifyButton = remember {
        {
            runCatching {
                startActivity(context, intent, null)
            }.onFailure {
                Toast.makeText(context, "No email app detected.", Toast.LENGTH_LONG).show()
            }
            Unit
        }
    }

    val onClickResendButton = remember {
        {
            emailAuthViewModel.apply {
                onEvent(EmailAuthEvent.ResendEmailVerification)
                onEvent(EmailAuthEvent.StartTimer)
                onEvent(EmailAuthEvent.SendEmailVerification)
            }
            Unit
        }
    }

    val onDismissNoInternetDialog = remember {{
        isNoInternetDialogVisible = false
    }}




    LaunchedEffect(key1 = true) {

        with(emailAuthViewModel) {
            onEvent(EmailAuthEvent.StartTimer)
            onEvent(EmailAuthEvent.SendEmailVerification)
            onEvent(EmailAuthEvent.SubscribeEmailVerification)

            eventFlow.collectLatest { event ->
                when (event) {
                    is EmailAuthUiEvent.EmailVerificationSuccess -> {
                        navController.navigateScreenInclusively(
                            Screens.MappingScreen.route,
                            Screens.EmailAuthScreen.route)
                    }

                    is EmailAuthUiEvent.ReloadEmailFailed -> {
                        Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                    }

                    is EmailAuthUiEvent.EmailVerificationNotSent -> {
                        Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                    }
                    is EmailAuthUiEvent.NoInternetConnection -> {
                        isNoInternetDialogVisible = true
                    }
                    is EmailAuthUiEvent.TimerStarted -> {
                        isTimerRunning = true
                    }
                    is EmailAuthUiEvent.TimerStopped -> {
                        isTimerRunning = false
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

                    else -> {
                        Timber.d("User email is not verified yet. Verification is not success.")
                    }
                }
            }
        }

    }


    EmailAuthScreenContent(
        modifier = Modifier.padding(paddingValues),
        emailAuthState = emailAuthState,
        isDarkTheme = isDarkTheme,
        onDismissAlertDialog = onDismissAlertDialog,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        onClickVerifyButton = onClickVerifyButton,
        onClickResendButton = onClickResendButton,
        alertDialogState = alertDialogState,
        isTimerRunning = isTimerRunning,
        isNoInternetDialogVisible = isNoInternetDialogVisible,
        )

}

@Preview()
@Composable
fun EmailAuthScreenPreview() {
    CyclistanceTheme(true) {
        EmailAuthScreenContent(
            isDarkTheme = true,
            emailAuthState = EmailAuthState(
                savedAccountEmail = "johndoe@gmail.com",
                secondsLeft = 10,
            ),
            alertDialogState = AlertDialogState(
                title = "Sample Title",
                description = "Sample Description",
                icon = R.drawable.ic_check_icon
            ),
            isTimerRunning = true,
            isNoInternetDialogVisible = false,
        )
    }

}


@Composable
fun EmailAuthScreenContent(
    modifier: Modifier = Modifier,
    emailAuthState: EmailAuthState = EmailAuthState(),
    isDarkTheme: Boolean,
    alertDialogState: AlertDialogState,
    isTimerRunning: Boolean,
    isNoInternetDialogVisible: Boolean,
    onDismissAlertDialog: () -> Unit = {},
    onClickVerifyButton: () -> Unit = {},
    onClickResendButton: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {}) {

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


            if (alertDialogState.visible()) {
                AlertDialog(
                    alertDialog = alertDialogState,
                    onDismissRequest = onDismissAlertDialog
                )
            }

            if (emailAuthState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(
                        AuthenticationConstraintsItem.ProgressBar.layoutId))
            }


            val secondsRemainingText = remember(isTimerRunning, emailAuthState.secondsLeft) {
                if (isTimerRunning) "Resend Email in ${emailAuthState.secondsLeft}s" else "Resend Email"
            }

            EmailAuthVerifyEmailButton(
                onClickVerifyButton = onClickVerifyButton,
                enabled = !emailAuthState.isLoading)

            EmailAuthResendButton(
                text = secondsRemainingText,
                isEnabled = !isTimerRunning && !emailAuthState.isLoading,
                onClickResendButton = onClickResendButton)

            if (isNoInternetDialogVisible) {
                NoInternetDialog(
                    onDismiss = onDismissNoInternetDialog,
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
                )
            }

        }


    }
}
