package com.myapp.cyclistance.feature_messaging.domain.use_case.manage_user

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetMessagingUserUseCase(private val repository: MessagingRepository) {
    suspend operator fun invoke(uid: String) = repository.getMessagingUser(uid)
}