package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.feature_authentication.data.repository.AuthRepositoryImpl
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.*
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.CreateWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithCredentialUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.get_account_info.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.google.firebase.auth.AuthCredential
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
 object AuthenticationModule {

    @Provides
    @ViewModelScoped
     fun provideAuthRepository(@ApplicationContext context:Context): AuthRepository<AuthCredential>{
         return AuthRepositoryImpl(context = context)
     }



    @Provides
    @ViewModelScoped
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
        )

}