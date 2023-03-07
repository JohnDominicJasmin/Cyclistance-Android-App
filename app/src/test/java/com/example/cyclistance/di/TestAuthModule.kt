package com.example.cyclistance.di

import com.example.cyclistance.feature_authentication.data.repositories.FakeAuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*

object TestAuthModule {
    private val fakeAuthRepository: FakeAuthRepository = FakeAuthRepository()

    fun setEmailReloaded(value: Boolean){
        fakeAuthRepository.reloadEmail(value)
    }

    fun setSignOut(value: Boolean){
        fakeAuthRepository.signOut(value)
    }

    fun sendEmailVerification(value: Boolean){
        fakeAuthRepository.sendEmailVerification(value)
    }

    fun setEmail(value: String){
        fakeAuthRepository.setEmail(value)
    }

    fun setPassword(value: String){
        fakeAuthRepository.setPassword(value)
    }

    fun setName(value: String){
        fakeAuthRepository.setName(value)
    }

    fun setPhoneNumber(value: String){
        fakeAuthRepository.setPhoneNumber(value)
    }

    fun setId(value: String){
        fakeAuthRepository.setId(value)
    }

    fun setPhotoUrl(value: String){
        fakeAuthRepository.setPhotoUrl(value)
    }

    fun isSignedInWithProvider(value: Boolean){
        fakeAuthRepository.isSignedInWithProvider(value)
    }

    fun isEmailVerified(value: Boolean){
        fakeAuthRepository.isEmailVerified(value)
    }

    fun hasAccountSignedIn(value: Boolean){
        fakeAuthRepository.hasAccountSignedIn(value)
    }

    fun setImagePath(value: String){
        fakeAuthRepository.setImagePath(value)
    }

    fun shouldReturnError(value: Boolean){
        fakeAuthRepository.shouldReturnNetworkError(value)
    }




    fun provideTestAuthUseCase(): AuthenticationUseCase {
        val repository = fakeAuthRepository
        return AuthenticationUseCase(
            reloadEmailUseCase = ReloadEmailUseCase(repository = repository),
            signOutUseCase = SignOutUseCase(repository = repository),
            createWithEmailAndPasswordUseCase = CreateWithEmailAndPasswordUseCase(repository = repository),
            getEmailUseCase = GetEmailUseCase(repository = repository),
            getNameUseCase = GetNameUseCase(repository = repository),
            getPhoneNumberUseCase = GetPhoneNumberUseCase(repository = repository),
            getPhotoUrlUseCase = GetPhotoUrlUseCase(repository = repository),
            getIdUseCase = GetIdUseCase(repository = repository),
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository),
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),
            updateProfileUseCase = UpdateProfileUseCase(repository = repository),
            updatePhoneNumberUseCase = UpdatePhoneNumberUseCase(repository = repository),
            uploadImageUseCase = UploadImageUseCase(repository = repository)
        )
    }
}