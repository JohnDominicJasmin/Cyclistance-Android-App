package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.use_case.create_account.CreateUserUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.CreateWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithCredentialUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.SignInWithEmailAndPasswordUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.GetEmailUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.GetIdUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.HasAccountSignedInUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.IsEmailVerifiedUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.IsSignedInWithProviderUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.ReloadEmailUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.SendEmailVerificationUseCase

data class AuthenticationUseCase(
    val reloadEmailUseCase: ReloadEmailUseCase,
    val signOutUseCase: SignOutUseCase,
    val createWithEmailAndPasswordUseCase: CreateWithEmailAndPasswordUseCase,
    val getEmailUseCase: GetEmailUseCase,
    val getIdUseCase: GetIdUseCase,
    val hasAccountSignedInUseCase: HasAccountSignedInUseCase,
    val isEmailVerifiedUseCase: IsEmailVerifiedUseCase,
    val isSignedInWithProviderUseCase: IsSignedInWithProviderUseCase,
    val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    val createUserUseCase: CreateUserUseCase

    )
