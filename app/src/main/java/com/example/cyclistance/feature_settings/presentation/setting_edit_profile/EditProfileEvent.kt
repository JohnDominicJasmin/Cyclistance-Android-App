package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.graphics.Bitmap
import android.net.Uri

sealed class EditProfileEvent {

    object Save: EditProfileEvent()
    data class EnteredPhoneNumber(val phoneNumber: String): EditProfileEvent()
    data class EnteredName(val name: String): EditProfileEvent()
    object LoadPhoto: EditProfileEvent()
    object LoadName: EditProfileEvent()
    object LoadPhoneNumber: EditProfileEvent()
    data class SelectImageUri(val uri: Uri?): EditProfileEvent()
    data class SelectBitmapPicture(val bitmap: Bitmap?): EditProfileEvent()
    object SaveImageToGallery: EditProfileEvent()

}