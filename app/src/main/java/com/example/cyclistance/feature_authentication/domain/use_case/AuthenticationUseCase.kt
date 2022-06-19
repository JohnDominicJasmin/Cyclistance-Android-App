package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.use_case.create.*
import com.example.cyclistance.feature_authentication.domain.use_case.read.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verification.*

data class AuthenticationUseCase(
    val reloadEmailUseCase: ReloadEmailUseCase,
    val signOutUseCase: SignOutUseCase,
    val createWithEmailAndPasswordUseCase: CreateWithEmailAndPasswordUseCase,
    val getEmailUseCase: GetEmailUseCase,
    val getNameUseCase: GetNameUseCase,
    val getIdUseCase: GetIdUseCase,
    val getPhoneNumberUseCase: GetPhoneNumberUseCase,
    val getPhotoUrlUseCase: GetPhotoUrlUseCase,
    val hasAccountSignedInUseCase: HasAccountSignedInUseCase,
    val isEmailVerifiedUseCase: IsEmailVerifiedUseCase,
    val isSignedInWithProviderUseCase: IsSignedInWithProviderUseCase,
    val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    val updatePhoneNumberUseCase : UpdatePhoneNumberUseCase,
    val updateProfileUseCase : UpdateProfileUseCase,
    val uploadImageUseCase: UploadImageUseCase

    )
