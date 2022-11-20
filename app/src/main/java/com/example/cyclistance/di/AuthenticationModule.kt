package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.data.repository.AuthRepositoryImpl
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
 object AuthenticationModule {


    @Provides
    @Singleton
    fun provideFirebaseAuth(@ApplicationContext context: Context):FirebaseAuth{
        return FirebaseAuth.getInstance().apply{
            if(BuildConfig.DEBUG){
                useEmulator(context.getString(R.string.IpAddress), 3030)
            }
        }
    }

    @Provides
    @Singleton
     fun provideAuthRepository(@ApplicationContext context:Context, firebaseAuth: FirebaseAuth): AuthRepository<AuthCredential>{
         return AuthRepositoryImpl(context = context, auth = firebaseAuth, )
     }



    @Provides
    @Singleton
    fun provideAuthenticationUseCase(repository: AuthRepository<AuthCredential>, @ApplicationContext context: Context):AuthenticationUseCase =
        AuthenticationUseCase(
            reloadEmailUseCase = ReloadEmailUseCase(repository = repository),
            signOutUseCase = SignOutUseCase(repository = repository),
            createWithEmailAndPasswordUseCase = CreateWithEmailAndPasswordUseCase(repository = repository, context = context),
            getEmailUseCase = GetEmailUseCase(repository = repository),
            getNameUseCase = GetNameUseCase(repository = repository),
            getPhoneNumberUseCase = GetPhoneNumberUseCase(repository = repository),
            getPhotoUrlUseCase = GetPhotoUrlUseCase(repository = repository),
            getIdUseCase = GetIdUseCase(repository = repository),
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository, context = context),
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),
            updateProfileUseCase = UpdateProfileUseCase(repository = repository),
            updatePhoneNumberUseCase = UpdatePhoneNumberUseCase(repository = repository),
            uploadImageUseCase = UploadImageUseCase(repository = repository)
        )

}