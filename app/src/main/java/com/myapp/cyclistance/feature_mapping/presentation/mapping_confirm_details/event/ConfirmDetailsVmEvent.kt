package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

import com.myapp.cyclistance.feature_mapping.domain.model.ConfirmationDetails


sealed class ConfirmDetailsVmEvent{
    data class ConfirmDetails(
      val confirmDetailsModel: ConfirmationDetails
    ): ConfirmDetailsVmEvent()

}
