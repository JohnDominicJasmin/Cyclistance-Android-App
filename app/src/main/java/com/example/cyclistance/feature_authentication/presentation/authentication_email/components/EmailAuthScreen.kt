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
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.MappingViewModel
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEventResult
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.navigation.Screens
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun EmailAuthScreen(
    onBackPressed:() -> Unit,
    navController: NavController?,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel()) {


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
                            navController?.navigate(Screens.NoInternetScreen.route) {
                                launchSingleTop = true
                            }
                        }
                        is EmailAuthEventResult.ShowAlertDialog -> {
                            alertDialogState = AlertDialogData(
                                title = event.title,
                                description = event.description,
                                resId = event.imageResId)
                        }
                        is EmailAuthEventResult.ShowMappingScreen -> {
                            navController?.navigate(Screens.MappingScreen.route) {
                                popUpTo(Screens.EmailAuthScreen.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
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