package com.example.cyclistance.di.messaging

import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetMessagingUserUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.UpdateUserAvailability
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.network.ReEnableNetworkSyncUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.notification.SendNotificationUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.seen.MarkAsSeenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object MessagingViewModelModule {



    @Provides
    @ViewModelScoped
    fun providesMessagingUseCase(repository: MessagingRepository): MessagingUseCase {
        return MessagingUseCase(
            refreshTokenUseCase = RefreshTokenUseCase(repository = repository),
            deleteTokenUseCase = DeleteTokenUseCase(repository = repository),
            sendMessageUseCase = SendMessageUseCase(repository = repository),
            addMessageListenerUseCase = AddMessageListenerUseCase(repository = repository),
            removeMessageListenerUseCase = RemoveMessageListenerUseCase(repository = repository),
            getUidUseCase = GetUidUseCase(repository = repository),
            getConversionIdUseCase = GetConversionIdUseCase(repository = repository),
            addConversionUseCase = AddConversionUseCase(repository = repository),
            updateConversionUseCase = UpdateConversionUseCase(repository = repository),
            updateUserAvailability = UpdateUserAvailability(repository = repository),
            sendNotificationUseCase = SendNotificationUseCase(repository = repository),
            getMessagingUserUseCase = GetMessagingUserUseCase(repository = repository),
            reEnableNetworkSyncUseCase = ReEnableNetworkSyncUseCase(repository = repository),
            markAsSeenUseCase = MarkAsSeenUseCase(repository = repository)
        )
    }
}