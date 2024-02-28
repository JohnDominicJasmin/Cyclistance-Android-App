package com.myapp.cyclistance.feature_emergency_call.domain.use_case.get_contact

import com.myapp.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository

class GetContactUseCase(private val repository: EmergencyContactRepository) {
    operator fun invoke(id: Int) = repository.getContact(id)
}