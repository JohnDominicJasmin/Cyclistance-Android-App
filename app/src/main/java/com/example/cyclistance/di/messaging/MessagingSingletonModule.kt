package com.example.cyclistance.di.messaging

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.feature_messaging.data.MessagingApi
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object MessagingSingletonModule {


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


}