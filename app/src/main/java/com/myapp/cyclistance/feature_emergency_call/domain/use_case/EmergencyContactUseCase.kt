package com.myapp.cyclistance.feature_emergency_call.domain.use_case

import com.myapp.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.AreContactsPurposelyDeletedUseCase
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.SetContactsPurposelyDeletedUseCase
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.delete_contact.DeleteContactUseCase
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.get_contact.GetContactUseCase
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.get_contact.GetContactsUseCase
import com.myapp.cyclistance.feature_emergency_call.domain.use_case.upsert_contact.UpsertContactUseCase

data class EmergencyContactUseCase(
    val deleteContactUseCase: DeleteContactUseCase,
    val getContactsUseCase: GetContactsUseCase,
    val upsertContactUseCase: UpsertContactUseCase,
    val areContactsPurposelyDeletedUseCase: AreContactsPurposelyDeletedUseCase,
    val setContactsPurposelyDeletedUseCase: SetContactsPurposelyDeletedUseCase,
    val getContactUseCase: GetContactUseCase
)
