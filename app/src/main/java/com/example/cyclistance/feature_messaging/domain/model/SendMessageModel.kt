package com.example.cyclistance.feature_messaging.domain.model

data class SendMessageModel(
    val receiverId: String = "",
    val message: String = "",
)
