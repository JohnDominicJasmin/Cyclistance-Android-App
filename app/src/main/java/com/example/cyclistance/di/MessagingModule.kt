package com.example.cyclistance.di

import android.content.Context
import androidx.annotation.Keep
import com.example.cyclistance.R
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Keep
@Module
@InstallIn(SingletonComponent::class)
object MessagingModule {


    @Provides
    @Singleton
    fun providesFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance().apply {
            isAutoInitEnabled = true
        }
    }


    @Provides
    @Singleton
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
    @Singleton
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

    @Provides
    @Singleton
    fun provideMessagingApi(@ApplicationContext context: Context): MessagingApi{
        return lazy {
            Retrofit.Builder()
                .baseUrl(context.getString(R.string.FcmBaseUrl))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(MessagingApi::class.java)
        }.value
    }


}