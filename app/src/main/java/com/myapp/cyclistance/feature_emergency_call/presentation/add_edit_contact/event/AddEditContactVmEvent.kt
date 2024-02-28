package com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event

import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

sealed class AddEditContactVmEvent{

    data class SaveContact(val emergencyContactModel: EmergencyContactModel) :
        AddEditContactVmEvent()

}
