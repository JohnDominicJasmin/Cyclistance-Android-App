package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileState

@Composable
fun EditProfileState.isUserInformationChanges(): Boolean {
    val result by derivedStateOf {
        nameSnapshot != name ||
        phoneNumberSnapshot != phoneNumber ||
        imageBitmap.bitmap != null
    }
    return result
}