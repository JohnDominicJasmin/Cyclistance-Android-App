package com.myapp.cyclistance.di

import com.myapp.cyclistance.feature_authentication.data.repositories.FakeAuthRepository
import com.myapp.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.create_account.CreateUserUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.create_account.CreateWithEmailAndPasswordUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithCredentialUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithEmailAndPasswordUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.read_account.GetEmailUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.read_account.GetIdUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.ChangePasswordUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.HasAccountSignedInUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.IsEmailVerifiedUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.IsSignedInWithProviderUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.ReloadEmailUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.SendEmailVerificationUseCase
import com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account.SendPasswordResetEmailUseCase

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
            changePasswordUseCase = ChangePasswordUseCase(repository),
            createUserUseCase = CreateUserUseCase(repository),
            sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(repository)
        )
    }
}