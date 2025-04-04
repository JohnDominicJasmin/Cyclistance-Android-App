package com.myapp.cyclistance.feature_authentication.presentation.auth_email

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.contexts.openEmailApp
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.components.EmailAuthScreenContent
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.event.EmailAuthEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.event.EmailAuthUiEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.event.EmailAuthVmEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.state.EmailAuthUiState
import com.myapp.cyclistance.navigation.Screens
import com.myapp.cyclistance.navigation.nav_graph.navigateScreenInclusively
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@Composable
fun EmailAuthScreen(
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {


    val context = LocalContext.current

    val emailAuthState by emailAuthViewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(EmailAuthUiState()) }




    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val onClickVerifyButton = remember {
        {
            runCatching {
                context.openEmailApp()
            }.onFailure {
                Toast.makeText(context, "No email app detected.", Toast.LENGTH_LONG).show()
            }
            Unit
        }
    }

    val onClickResendButton = remember {
        {
            emailAuthViewModel.apply {
                onEvent(EmailAuthVmEvent.ResendEmailVerification)
                onEvent(EmailAuthVmEvent.StartTimer)
                onEvent(EmailAuthVmEvent.SendEmailVerification)
            }
            Unit
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
        emailAuthViewModel.onEvent(EmailAuthVmEvent.StartTimer)
        emailAuthViewModel.onEvent(EmailAuthVmEvent.SendEmailVerification)
        emailAuthViewModel.onEvent(EmailAuthVmEvent.SubscribeEmailVerification)
    }



    LaunchedEffect(key1 = true) {

        emailAuthViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EmailAuthEvent.EmailVerificationSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.MappingNavigation.ROUTE,
                        Screens.AuthenticationNavigation.ROUTE)
                }

                is EmailAuthEvent.ReloadEmailFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthEvent.EmailVerificationNotSent -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                }

                is EmailAuthEvent.NoInternetConnection -> {
                    uiState = uiState.copy(isNoInternetVisible = true)
                }

                is EmailAuthEvent.TimerStarted -> {
                    uiState = uiState.copy(isTimerRunning = true)
                }

                is EmailAuthEvent.TimerStopped -> {
                    uiState = uiState.copy(isTimerRunning = false)
                }

                is EmailAuthEvent.EmailVerificationSent -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "New Email Sent.",
                            description = "New verification email has been sent to your email address.",
                            icon = R.raw.success
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

                else -> {
                    Timber.d("User email is not verified yet. Verification is not success.")
                }
            }
        }

    }


    EmailAuthScreenContent(
        modifier = Modifier.padding(paddingValues),
        emailAuthState = emailAuthState,
        uiState = uiState,
        event = { event ->
            when (event) {
                is EmailAuthUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is EmailAuthUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is EmailAuthUiEvent.VerifyEmailAuth -> onClickVerifyButton()
                is EmailAuthUiEvent.ResendEmailAuth -> onClickResendButton()
            }
        }
    )

}
