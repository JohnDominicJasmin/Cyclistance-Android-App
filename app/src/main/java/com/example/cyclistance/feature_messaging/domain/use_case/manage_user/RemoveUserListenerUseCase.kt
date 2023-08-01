package com.example.cyclistance.feature_messaging.domain.use_case.manage_user

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class RemoveUserListenerUseCase(private val repository: MessagingRepository) {
    operator fun invoke(){
        repository.removeUserListener()
    }
}