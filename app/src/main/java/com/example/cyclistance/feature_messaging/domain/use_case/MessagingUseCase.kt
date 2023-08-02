package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.feature_messaging.domain.use_case.chat.AddChatListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.chat.RemoveChatListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.AddUserListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.RemoveUserListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.UpdateUserAvailability
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase

data class MessagingUseCase(
    val refreshTokenUseCase: RefreshTokenUseCase,
    val addUserListenerUseCase: AddUserListenerUseCase,
    val removeUserListenerUseCase: RemoveUserListenerUseCase,
    val deleteTokenUseCase: DeleteTokenUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val addMessageListenerUseCase: AddMessageListenerUseCase,
    val removeMessageListenerUseCase: RemoveMessageListenerUseCase,
    val getUidUseCase: GetUidUseCase,
    val getConversionIdUseCase: GetConversionIdUseCase,
    val addConversionUseCase: AddConversionUseCase,
    val updateConversionUseCase: UpdateConversionUseCase,
    val addChatListenerUseCase: AddChatListenerUseCase,
    val removeChatListenerUseCase: RemoveChatListenerUseCase,
    val updateUserAvailability: UpdateUserAvailability
)
