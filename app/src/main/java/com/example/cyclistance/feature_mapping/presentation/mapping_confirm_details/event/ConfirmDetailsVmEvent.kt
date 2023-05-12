package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetailsModel


sealed class ConfirmDetailsVmEvent{
    data class ConfirmDetails(
      val confirmDetailsModel: ConfirmationDetailsModel
    ): ConfirmDetailsVmEvent()

}
