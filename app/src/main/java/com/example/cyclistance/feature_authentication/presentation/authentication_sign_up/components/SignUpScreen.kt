package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpEventResult
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.AppImageIcon
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.mapping_screen.MappingViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    navigateTo : (destination: String, popUpToDestination: String?) -> Unit) {

    val stateValue = signUpViewModel.state.value

    with(stateValue) {

        val hasAccountSignedIn = remember { signUpViewModel.hasAccountSignedIn() }
        val isUserCreatedNewAccount = remember { email.text != mappingViewModel.getEmail() }
        var alertDialogState by remember { mutableStateOf(AlertDialogData()) }
        val context = LocalContext.current

        val signUpAccount = {
            if (hasAccountSignedIn && isUserCreatedNewAccount) {
                mappingViewModel.signOutAccount()
            }
            signUpViewModel.onEvent(SignUpEvent.SignUp)
        }


        LaunchedEffect(key1 = true){
            signUpViewModel.eventFlow.collectLatest{ event ->

                when(event){
                    is SignUpEventResult.ShowNoInternetScreen -> {
                        navigateTo(Screens.NoInternetScreen.route, null)
                    }
                    is SignUpEventResult.ShowAlertDialog -> {
                        alertDialogState = AlertDialogData(
                            title = event.title,
                            description = event.description,
                            resId = event.imageResId)
                    }
                    is SignUpEventResult.ShowEmailAuthScreen -> {
                        navigateTo(Screens.EmailAuthScreen.route, Screens.SignUpScreen.route)
                    }
                    is SignUpEventResult.ShowToastMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
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
                constraintSet = signUpConstraints,
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)) {

                AppImageIcon(layoutId = AuthenticationConstraintsItem.IconDisplay.layoutId)
                SignUpTextArea()

                Waves(
                    topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                    bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
                )

                if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                    SetupAlertDialog(
                        alertDialog = alertDialogState,
                        onDismissRequest = {
                            alertDialogState = AlertDialogData()
                        })
                }

                SignUpTextFieldsArea(
                    state = this@with,
                    signUpViewModel = signUpViewModel,
                    keyboardActionOnDone = { signUpAccount() }
                )


                SignUpButton(onClickButton = {
                    signUpAccount()
                })

                SignUpClickableText() {
                    navigateTo(Screens.SignInScreen.route, Screens.SignUpScreen.route)
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                    )
                }
            }
        }
    }

}

