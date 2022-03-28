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
            reloadEmailUseCase = ReloadEmailUseCase(repository = repository),// EmailScreen & SignInScreen(EmailAuthViewModel)
            signOutUseCase = SignOutUseCase(repository = repository),//Mapping Screen
            createWithEmailAndPasswordUseCase = CreateWithEmailAndPasswordUseCase(repository = repository, context = context), // Sign Up Screen
            getEmailUseCase = GetEmailUseCase(repository = repository), // Main screen
            getNameUseCase = GetNameUseCase(repository = repository),// Main screen
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),//SplashScreen & Sign Up Screen
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),// Email, IntroSlider, Sign In(EmailAuthViewModel)
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),//SplashScreen for choosing what screen to show (IntroSlider, Mapping Screen, Sign In Screen)//
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),// Email Screen
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository, context = context),// Sign in Screen
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),//Sign In Screen
        )

}