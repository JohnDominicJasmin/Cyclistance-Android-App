package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialogData
import com.example.cyclistance.feature_alert_dialog.presentation.SetupAlertDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEventResult
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun EmailAuthScreen(
    onBackPressed:() -> Unit,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    navigateTo : (destination: String, popUpToDestination: String?) -> Unit) {


    var alertDialogState by remember { mutableStateOf(AlertDialogData()) }
    val context = LocalContext.current
    val email = remember { mappingViewModel.getEmail() }

    BackHandler(enabled = true, onBack = onBackPressed)

    with(emailAuthViewModel.state.value) {

        LaunchedEffect(key1 = true) {

            with(emailAuthViewModel) {
                onEvent(EmailAuthEvent.StartTimer)
                onEvent(EmailAuthEvent.SendEmailVerification)
                onEvent(EmailAuthEvent.SubscribeEmailVerification)

                eventFlow.collectLatest { event ->

                    when (event) {
                        is EmailAuthEventResult.ShowNoInternetScreen -> {
                            navigateTo(Screens.NoInternetScreen.route, null)
                        }
                        is EmailAuthEventResult.ShowAlertDialog -> {
                            alertDialogState = AlertDialogData(
                                title = event.title,
                                description = event.description,
                                resId = event.imageResId)
                        }
                        is EmailAuthEventResult.ShowMappingScreen -> {
                            navigateTo(Screens.MappingScreen.route, Screens.EmailAuthScreen.route)
                        }
                        is EmailAuthEventResult.ShowToastMessage -> {
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
                .background(BackgroundColor)) {


            ConstraintLayout(
                constraintSet = emailAuthConstraints,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(BackgroundColor)) {


                EmailIcon()
                EmailAuthTextStatus(email = email)

                if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                    SetupAlertDialog(
                        alertDialog = alertDialogState,
                        onDismissRequest = {
                            alertDialogState = AlertDialogData()
                        })
                }
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.layoutId(
                            AuthenticationConstraintsItem.ProgressBar.layoutId))
                }

                val secondsRemaining  = if (secondsLeft < 2) "$secondsLeft" else "$secondsLeft s"
                val secondsRemainingText by derivedStateOf { if (isTimerRunning) "Resend E-mail in $secondsRemaining" else "Resend E-mail" }


                EmailAuthResendButton(
                    text = secondsRemainingText,
                    isEnabled = !isTimerRunning,
                    onClick = {
                        emailAuthViewModel.apply{
                            onEvent(EmailAuthEvent.StartTimer)
                            onEvent(EmailAuthEvent.ResendButtonClick)
                            onEvent(EmailAuthEvent.SendEmailVerification)
                        }
                    })


            }

        }
    }


}