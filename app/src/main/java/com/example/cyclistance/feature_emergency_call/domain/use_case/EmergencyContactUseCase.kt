package com.example.cyclistance.feature_emergency_call.domain.use_case

import com.example.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.AreContactsPurposelyDeletedUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.SetContactsPurposelyDeletedUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.delete_contact.DeleteContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.get_contacts.GetContactsUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact.UpsertContactUseCase

data class EmergencyContactUseCase(
    val deleteContactUseCase: DeleteContactUseCase,
    val getContactsUseCase: GetContactsUseCase,
    val upsertContactUseCase: UpsertContactUseCase,
    val areContactsPurposelyDeletedUseCase: AreContactsPurposelyDeletedUseCase,
    val setContactsPurposelyDeletedUseCase: SetContactsPurposelyDeletedUseCase
)
