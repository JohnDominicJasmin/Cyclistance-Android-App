package com.myapp.cyclistance.di.messaging

import com.myapp.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.myapp.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.manage_user.GetMessagingUserUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.network.ReEnableNetworkSyncUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.notification.SendNotificationUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.seen.MarkAsSeenUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.myapp.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase
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
            sendNotificationUseCase = SendNotificationUseCase(repository = repository),
            getMessagingUserUseCase = GetMessagingUserUseCase(repository = repository),
            reEnableNetworkSyncUseCase = ReEnableNetworkSyncUseCase(repository = repository),
            markAsSeenUseCase = MarkAsSeenUseCase(repository = repository)
        )
    }
}