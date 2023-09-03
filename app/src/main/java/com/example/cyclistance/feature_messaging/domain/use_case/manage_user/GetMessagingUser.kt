package com.example.cyclistance.feature_messaging.domain.use_case.manage_user

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class GetMessagingUser(private val repository: MessagingRepository) {
    suspend operator fun invoke(uid: String) = repository.getMessagingUser(uid)
}