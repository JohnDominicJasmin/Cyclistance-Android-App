package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.feature_authentication.data.repository.AuthRepositoryImpl
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.*
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
            registerAccountUseCase = RegisterAccountUseCase(repository = repository),
            getEmailUseCase = GetEmailUseCase(repository = repository),
            getNameUseCase = GetNameUseCase(repository = repository),
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository, context = context),
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),
        )

}