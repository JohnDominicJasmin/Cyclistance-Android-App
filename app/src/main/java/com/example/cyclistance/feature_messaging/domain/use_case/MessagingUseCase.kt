package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUsersUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase

data class MessagingUseCase(
    val refreshTokenUseCase: RefreshTokenUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val deleteTokenUseCase: DeleteTokenUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val addMessageListenerUseCase: AddMessageListenerUseCase,
    val removeMessageListenerUseCase: RemoveMessageListenerUseCase,
    val getUidUseCase: GetUidUseCase
)
