package com.example.cyclistance.di

import com.example.cyclistance.feature_authentication.data.repositories.FakeAuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.example.cyclistance.feature_settings.domain.use_case.GetNameUseCase
import com.example.cyclistance.feature_settings.domain.use_case.GetPhotoUrlUseCase

object TestAuthModule {
    private val fakeAuthRepository: FakeAuthRepository = FakeAuthRepository()


    operator fun invoke() = FakeAuthRepository.Companion

    fun provideTestAuthUseCase(): AuthenticationUseCase {
        val repository = fakeAuthRepository
        return AuthenticationUseCase(
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
        )
    }
}