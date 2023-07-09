package com.example.cyclistance.feature_emergency_call.domain.use_case.get_contact

import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository

class GetContactsUseCase(
    private val repository: EmergencyContactRepository
) {

    operator fun invoke() = repository.getContacts()
}