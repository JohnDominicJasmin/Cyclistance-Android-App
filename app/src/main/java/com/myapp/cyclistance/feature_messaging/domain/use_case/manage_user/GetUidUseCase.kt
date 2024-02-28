package com.myapp.cyclistance.feature_messaging.domain.use_case.manage_user

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetUidUseCase(private val repository: MessagingRepository) {
    operator fun invoke(): String {
        return repository.getUserUid()
    }
}