package com.example.cyclistance.di.messaging

import android.content.Context
import com.example.cyclistance.feature_messaging.data.MessagingApi
import com.example.cyclistance.feature_messaging.data.repository.MessagingRepositoryImpl
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.chat.AddChatListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.chat.RemoveChatListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.AddUserListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetMessagingUser
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.RemoveUserListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.UpdateUserAvailability
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.notification.SendNotificationUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.DeleteTokenUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object MessagingViewModelModule {

    @Provides
    @ViewModelScoped
    fun providesMessagingRepository(
        @ApplicationContext context: Context,
        firebaseFiresStore: FirebaseFirestore,
        firebaseMessaging: FirebaseMessaging,
        firebaseAuth: FirebaseAuth,
        api: MessagingApi): MessagingRepository {

        return MessagingRepositoryImpl(
            fireStore = firebaseFiresStore,
            firebaseMessaging = firebaseMessaging,
            auth = firebaseAuth,
            appContext = context,
            api = api
        )
    }

    @Provides
    @ViewModelScoped
    fun providesMessagingUseCase(repository: MessagingRepository): MessagingUseCase {
        return MessagingUseCase(
            refreshTokenUseCase = RefreshTokenUseCase(repository = repository),
            addUserListenerUseCase = AddUserListenerUseCase(repository = repository),
            deleteTokenUseCase = DeleteTokenUseCase(repository = repository),
            sendMessageUseCase = SendMessageUseCase(repository = repository),
            addMessageListenerUseCase = AddMessageListenerUseCase(repository = repository),
            removeMessageListenerUseCase = RemoveMessageListenerUseCase(repository = repository),
            getUidUseCase = GetUidUseCase(repository = repository),
            getConversionIdUseCase = GetConversionIdUseCase(repository = repository),
            addConversionUseCase = AddConversionUseCase(repository = repository),
            updateConversionUseCase = UpdateConversionUseCase(repository = repository),
            removeUserListenerUseCase = RemoveUserListenerUseCase(repository = repository),
            addChatListenerUseCase = AddChatListenerUseCase(repository = repository),
            removeChatListenerUseCase = RemoveChatListenerUseCase(repository = repository),
            updateUserAvailability = UpdateUserAvailability(repository = repository),
            sendNotificationUseCase = SendNotificationUseCase(repository = repository),
            getMessagingUserUseCase = GetMessagingUser(repository = repository)
        )
    }
}