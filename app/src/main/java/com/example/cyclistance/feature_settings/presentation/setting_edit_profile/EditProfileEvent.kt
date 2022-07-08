package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue

sealed class EditProfileEvent {

    object Save: EditProfileEvent()
    data class EnteredPhoneNumber(val phoneNumber: TextFieldValue): EditProfileEvent()
    data class EnteredName(val name: TextFieldValue): EditProfileEvent()
    object LoadPhoto: EditProfileEvent()
    object LoadName: EditProfileEvent()
    object LoadPhoneNumber: EditProfileEvent()
    data class SelectImageUri(val uri: Uri?): EditProfileEvent()
    data class SelectBitmapPicture(val bitmap: Bitmap?): EditProfileEvent()
    object SaveImageToGallery: EditProfileEvent()

}