package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.common.MappingConstants.NONE_OF_THE_ABOVE_RESULT_CODE
import com.example.cyclistance.common.MappingConstants.NO_SIM_CARD_RESULT_CODE
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.theme.*
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import timber.log.Timber

@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {


    val context = LocalContext.current
    val hintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build()

    val intent = Credentials.getClient(context).getHintPickerIntent(hintRequest)
    val intentSender = IntentSenderRequest.Builder(intent.intentSender).build()

    val resultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->

        when(result.resultCode){
            Activity.RESULT_OK ->{
                val credential: Credential? = result.data?.getParcelableExtra(Credential.EXTRA_KEY)
                editProfileViewModel.onEvent(event = EditProfileEvent.EnteredPhoneNumber(phoneNumber = TextFieldValue(text = credential!!.id)))
            }
            NO_SIM_CARD_RESULT_CODE ->{
                Toast.makeText(context, "No SIM Card Detected", Toast.LENGTH_LONG).show()
            }
            NONE_OF_THE_ABOVE_RESULT_CODE ->{
                editProfileViewModel.onEvent(event = EditProfileEvent.EnteredPhoneNumber(phoneNumber = TextFieldValue(text = "")))

            }


        }

    }




    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {


        val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText) = createRefs()

        ProfilePictureArea(modifier = Modifier
            .size(105.dp)
            .constrainAs(profilePictureArea) {
                top.linkTo(parent.top, margin = 18.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            })




        Text(
            text = "Change Profile Photo",
            color = Blue600,
            style = MaterialTheme.typography.caption, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.constrainAs(changePhotoText) {
                top.linkTo(profilePictureArea.bottom, margin = 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            })


        TextFieldInputArea(modifier = Modifier
            .constrainAs(textFieldInputArea) {
                top.linkTo(changePhotoText.bottom, margin = 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.percent(0.5f)
                width = Dimension.percent(0.9f)

            },
        editProfileViewModel = editProfileViewModel,
        onPhoneTextFieldClick = {
            resultLauncher.launch(intentSender)
        })


        MappingButtonNavigation(modifier = Modifier
            .constrainAs(buttonNavigationArea) {
                top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.percent(0.1f)
                width = Dimension.percent(0.8f)
            },
            positiveButtonText = "Save",
            onClickCancelButton = {
               /*todo*/
            },
            onClickConfirmButton = {
                /*todo*/
            })


    }
}


@Preview(device = Devices.NEXUS_5)
@Composable
fun EditProfileScreenPreview() {

    CyclistanceTheme(false) {
        EditProfileScreen { destination, popUpToDestination ->

        }
    }
}