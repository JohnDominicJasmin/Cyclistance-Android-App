package com.example.cyclistance.di

import android.content.Context
import androidx.annotation.Keep
import com.example.cyclistance.feature_messaging.data.repository.MessagingRepositoryImpl
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.AddConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.GetConversionIdUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.conversion.UpdateConversionUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUidUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUsersUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.AddMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.RemoveMessageListenerUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.message.SendMessageUseCase
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
        firebaseAuth: FirebaseAuth): MessagingRepository {

        return MessagingRepositoryImpl(
            fireStore = firebaseFiresStore,
            firebaseMessaging = firebaseMessaging,
            auth = firebaseAuth,
            appContext = context
        )
    }

    @Provides
    @Singleton
    fun providesMessagingUseCase(repository: MessagingRepository): MessagingUseCase {
        return MessagingUseCase(
            refreshTokenUseCase = RefreshTokenUseCase(repository = repository),
            getUsersUseCase = GetUsersUseCase(repository = repository),
            deleteTokenUseCase = DeleteTokenUseCase(repository = repository),
            sendMessageUseCase = SendMessageUseCase(repository = repository),
            addMessageListenerUseCase = AddMessageListenerUseCase(repository = repository),
            removeMessageListenerUseCase = RemoveMessageListenerUseCase(repository = repository),
            getUidUseCase = GetUidUseCase(repository = repository),
            getConversionIdUseCase = GetConversionIdUseCase(repository = repository),
            addConversionUseCase = AddConversionUseCase(repository = repository),
            updateConversionUseCase = UpdateConversionUseCase(repository = repository)
        )
    }


}