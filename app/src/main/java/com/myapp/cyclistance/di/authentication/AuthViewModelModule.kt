package com.myapp.cyclistance.di.authentication

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.cyclistance.feature_authentication.data.repository.AuthRepositoryImpl
import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object AuthViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): AuthRepository {

        return AuthRepositoryImpl(
            appContext = context,
            auth = firebaseAuth,
            fireStore = fireStore)
    }

    @Provides
    @ViewModelScoped
    fun provideAuthenticationUseCase(repository: AuthRepository): AuthenticationUseCase =
        AuthenticationUseCase(
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
            createUserUseCase = CreateUserUseCase(repository = repository),
            sendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(repository = repository),
            changePasswordUseCase = ChangePasswordUseCase(repository = repository),
        )

}