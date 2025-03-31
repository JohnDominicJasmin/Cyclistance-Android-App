package com.myapp.cyclistance.feature_authentication.domain.use_case

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
    val createUserUseCase: CreateUserUseCase,
    val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    val changePasswordUseCase: ChangePasswordUseCase)
