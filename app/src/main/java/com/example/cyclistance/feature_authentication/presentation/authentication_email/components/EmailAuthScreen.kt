package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingViewModel
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialogData
import com.example.cyclistance.feature_alert_dialog.presentation.SetupAlertDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthUiEvent
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun EmailAuthScreen(
    isDarkTheme: Boolean = false,
    onBackPressed:() -> Unit,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel(),
    navigateTo : (destination: String, popUpToDestination: String?) -> Unit) {


    var alertDialogState by remember { mutableStateOf(AlertDialogData()) }
    val context = LocalContext.current
    val email = remember { mappingViewModel.getEmail() }

    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


    BackHandler(enabled = true, onBack = onBackPressed)

    with(emailAuthViewModel.state.value) {

        LaunchedEffect(key1 = true) {

            with(emailAuthViewModel) {
                onEvent(EmailAuthEvent.StartTimer)
                onEvent(EmailAuthEvent.SendEmailVerification)
                onEvent(EmailAuthEvent.SubscribeEmailVerification)

                eventFlow.collectLatest { event ->

                    when (event) {
                        is EmailAuthUiEvent.ShowNoInternetScreen -> {
                            navigateTo(Screens.NoInternetScreen.route, null)
                        }
                        is EmailAuthUiEvent.ShowAlertDialog -> {
                            alertDialogState = AlertDialogData(
                                title = event.title,
                                description = event.description,
                                resId = event.imageResId)
                        }
                        is EmailAuthUiEvent.ShowMappingScreen -> {
                            navigateTo(Screens.MappingScreen.route, Screens.EmailAuthScreen.route)
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
                .background(MaterialTheme.colors.background)) {


            ConstraintLayout(
                constraintSet = emailAuthConstraints,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(MaterialTheme.colors.background)) {

                Image(
                    contentDescription = "App Icon",
                    painter = painterResource(id = if(isDarkTheme) R.drawable.ic_dark_email else R.drawable.ic_light_email),
                    modifier = Modifier
                        .height(165.dp)
                        .width(155.dp)
                        .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)

                )

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
                val secondsRemainingText by derivedStateOf { if (isTimerRunning) "Resend Email in $secondsRemaining" else "Resend Email" }

                EmailAuthVerifyEmailButton(onClick = {
                    kotlin.runCatching {
                        startActivity(context,intent, null)
                    }.onFailure {
                        Toast.makeText(context, "No email app detected.", Toast.LENGTH_LONG).show()
                    }
                })

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

@Preview(device = Devices.NEXUS_5)
@Composable
fun EmailAuthScreenPreview() {
    val state  = false
    CyclistanceTheme(state){
        var alertDialogState by remember { mutableStateOf(AlertDialogData()) }
        val email = remember { "Mikocabal27@gmail.com" }
        val context = LocalContext.current
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Column(

                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material.MaterialTheme.colors.background)) {


                ConstraintLayout(
                    constraintSet = com.example.cyclistance.feature_authentication.presentation.authentication_email.components.emailAuthConstraints,
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxHeight(0.9f)
                        .background(androidx.compose.material.MaterialTheme.colors.background)) {


                    Image(
                        contentDescription = null,
                        painter = painterResource(if(state) R.drawable.ic_dark_email else R.drawable.ic_light_email),
                        modifier = Modifier
                            .height(165.dp)
                            .width(155.dp)
                            .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)

                    )

                    EmailAuthTextStatus(email = email)

                    if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                        SetupAlertDialog(
                            alertDialog = alertDialogState,
                            onDismissRequest = {
                                alertDialogState = AlertDialogData()
                            })
                    }
                    if (true) {
                        CircularProgressIndicator(
                            modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId))
                    }


                    EmailAuthVerifyEmailButton(onClick = {
                        startActivity(context, intent, null)
                    })

                    EmailAuthResendButton(
                        text = "Resend Email in 20s",
                        isEnabled = true,
                        onClick = {

                        })


                }

            }
        }

    }
