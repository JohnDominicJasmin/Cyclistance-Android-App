package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetMessagingUserUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.network.ReEnableNetworkSyncUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.notification.SendNotificationUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.seen.MarkAsSeenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase

data class MessagingUseCase(
    val refreshTokenUseCase: RefreshTokenUseCase,
    val deleteTokenUseCase: DeleteTokenUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val addMessageListenerUseCase: AddMessageListenerUseCase,
    val removeMessageListenerUseCase: RemoveMessageListenerUseCase,
    val getUidUseCase: GetUidUseCase,
    val getConversionIdUseCase: GetConversionIdUseCase,
    val addConversionUseCase: AddConversionUseCase,
    val updateConversionUseCase: UpdateConversionUseCase,
    val sendNotificationUseCase: SendNotificationUseCase,
    val getMessagingUserUseCase: GetMessagingUserUseCase,
    val reEnableNetworkSyncUseCase: ReEnableNetworkSyncUseCase,
    val markAsSeenUseCase: MarkAsSeenUseCase
)
