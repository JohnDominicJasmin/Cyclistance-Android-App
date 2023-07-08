package com.example.cyclistance.feature_emergency_call.domain.use_case

import com.example.cyclistance.feature_emergency_call.domain.use_case.delete_contact.DeleteContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.get_contacts.GetContactsUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact.UpsertContactUseCase

data class EmergencyContactUseCase(
    val deleteContactUseCase: DeleteContactUseCase,
    val getContactsUseCase: GetContactsUseCase,
    val upsertContactUseCase: UpsertContactUseCase
)
