package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetailsModel


sealed class ConfirmDetailsEvent{
    data class ConfirmDetails(
      val confirmDetailsModel: ConfirmationDetailsModel
    ): ConfirmDetailsEvent()

}
