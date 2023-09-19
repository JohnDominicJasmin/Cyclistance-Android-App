package com.example.cyclistance.di.messaging

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.feature_messaging.data.MessagingApi
import com.example.cyclistance.feature_messaging.data.repository.MessagingRepositoryImpl
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
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
import javax.inject.Named
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object MessagingDataSourceModule {


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
    fun providesFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance().apply {
            isAutoInitEnabled = true
        }
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

    @Provides
    @Singleton
    @Named("messagingNotification")
    fun provideMessagingNotification(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return lazy {
            NotificationCompat.Builder(
                context,
                MessagingConstants.CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_app_notification_icon)
                priority = NotificationCompat.PRIORITY_HIGH
                setAutoCancel(true)
                setShowWhen(true)
                setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            }

        }.value
    }


}