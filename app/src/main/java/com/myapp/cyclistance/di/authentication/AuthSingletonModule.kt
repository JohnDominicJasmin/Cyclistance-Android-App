package com.myapp.cyclistance.di.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myapp.cyclistance.BuildConfig
import com.myapp.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.myapp.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthSingletonModule {

    //emulator host = 10.0.2.2
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance().apply {
            if (BuildConfig.DEBUG) {
                useEmulator("192.168.18.21", 9099)
            }
        }
    }


    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        return Firebase.firestore.apply {
            if (BuildConfig.DEBUG) {
                useEmulator("192.168.18.21", 9299)
            }
            firestoreSettings = settings
        }
    }



}