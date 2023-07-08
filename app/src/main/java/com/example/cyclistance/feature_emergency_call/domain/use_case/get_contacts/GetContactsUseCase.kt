package com.example.cyclistance.feature_emergency_call.domain.use_case.get_contacts

import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: EmergencyContactRepository
) {

    operator fun invoke() = repository.getContacts()
}