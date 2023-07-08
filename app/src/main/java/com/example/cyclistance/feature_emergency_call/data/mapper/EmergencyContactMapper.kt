package com.example.cyclistance.feature_emergency_call.data.mapper

import com.example.cyclistance.feature_emergency_call.data.data_source.local.entities.EmergencyContact
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel

object EmergencyContactMapper {

    fun EmergencyContact.toEmergencyContactModel(): EmergencyContactModel {
        return EmergencyContactModel(
            id = this.id,
            name = this.name,
            photo = photoUri,
            phoneNumber = this.number
        )
    }

    fun EmergencyContactModel.toEmergencyContact(): EmergencyContact {
        return EmergencyContact(
            name = this.name,
            photoUri = photo,
            number = this.phoneNumber
        )
    }

}