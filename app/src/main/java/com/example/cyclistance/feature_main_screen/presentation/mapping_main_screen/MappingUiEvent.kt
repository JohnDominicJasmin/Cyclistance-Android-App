package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import androidx.annotation.RawRes
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpUiEvent
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase

sealed class MappingUiEvent{
    object ShowNoInternetScreen: MappingUiEvent()
    object ShowConfirmDetailsScreen: MappingUiEvent()
    data class ShowToastMessage(val message: String): MappingUiEvent()

    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ): MappingUiEvent()

}
