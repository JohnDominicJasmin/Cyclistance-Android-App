package com.example.cyclistance.di

import android.content.Context
import androidx.annotation.Keep
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.feature_authentication.data.repository.AuthRepositoryImpl
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Keep
@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

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


    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): AuthRepository {

        return AuthRepositoryImpl(
            appContext = context,
            auth = firebaseAuth,
            fireStore = fireStore)
    }


    @Provides
    @Singleton
    fun provideAuthenticationUseCase(repository: AuthRepository): AuthenticationUseCase =
        AuthenticationUseCase(
            reloadEmailUseCase = ReloadEmailUseCase(repository = repository),
            signOutUseCase = SignOutUseCase(repository = repository),
            createWithEmailAndPasswordUseCase = CreateWithEmailAndPasswordUseCase(repository = repository),
            getEmailUseCase = GetEmailUseCase(repository = repository),
            getIdUseCase = GetIdUseCase(repository = repository),
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository),
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),
            createUserUseCase = CreateUserUseCase(repository = repository),
            sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(repository = repository)
        )


}