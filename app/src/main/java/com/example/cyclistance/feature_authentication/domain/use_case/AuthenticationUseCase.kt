package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.use_case.create_account.CreateWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithCredentialUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.get_account_info.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*

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
    )
