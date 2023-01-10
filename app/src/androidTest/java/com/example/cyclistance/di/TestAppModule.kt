package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.SetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.SetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.CreateRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.*
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.BroadcastTransactionLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.GetTransactionLocationUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.BroadcastUserUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.GetUserUpdatesUseCase
import cyclistance.repositories.FakeAuthRepository
import cyclistance.repositories.FakeMappingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_mapping_repository")
    fun provideFakeMappingRepository(): MappingRepository {
        return FakeMappingRepository()
    }



    @Provides
    @Named("test_mapping_use_case")
    fun provideTestMappingUseCase(@Named("test_mapping_repository") repository: MappingRepository, @ApplicationContext context: Context): MappingUseCase {
        return MappingUseCase(
            getUsersUseCase = GetUsersUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            createUserUseCase = CreateUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),

            getRescueTransactionByIdUseCase = GetRescueTransactionByIdUseCase(repository),
            createRescueTransactionUseCase = CreateRescueTransactionUseCase(repository),
            deleteRescueTransactionUseCase = DeleteRescueTransactionUseCase(repository),


            getUserLocationUseCase = GetUserLocationUseCase(repository),
            getFullAddressUseCase = GetFullAddressUseCase(repository),
            getBikeTypeUseCase = GetBikeTypeUseCase(repository),
            setBikeTypeUseCase = SetBikeTypeUseCase(repository),
            getAddressUseCase = GetAddressUseCase(repository),
            setAddressUseCase = SetAddressUseCase(repository),
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(repository, context),
            broadcastUserUseCase = BroadcastUserUseCase(repository, context),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(context, repository),
            getUserUpdatesUseCase = GetUserUpdatesUseCase(context, repository),
            broadcastTransactionLocationUseCase = BroadcastTransactionLocationUseCase(repository, context),
            getTransactionLocationUpdatesUseCase = GetTransactionLocationUpdatesUseCase(context, repository),
            deleteRescueRespondentUseCase = DeleteRescueRespondentUseCase(repository),
            addRescueRespondentUseCase = AddRescueRespondentUseCase(repository),
            deleteAllRespondentsUseCase = DeleteAllRespondentsUseCase(repository),
            confirmDetailsUseCase = ConfirmDetailsUseCase(repository),
            confirmCancellationUseCase = ConfirmCancellationUseCase(repository),
            getRouteDirectionsUseCase = GetRouteDirectionsUseCase(repository, context),
        )
    }

    @Provides
    @Named("test_auth_repository")
    fun provideFakeAuthRepository(): AuthRepository{
        return FakeAuthRepository()
    }

    @Provides
    @Named("test_auth_use_case")
    fun provideTestAuthUseCase(@Named("test_auth_repository") repository: AuthRepository, @ApplicationContext context: Context): AuthenticationUseCase{
       return AuthenticationUseCase(
            reloadEmailUseCase = ReloadEmailUseCase(repository = repository),
            signOutUseCase = SignOutUseCase(repository = repository),
            createWithEmailAndPasswordUseCase = CreateWithEmailAndPasswordUseCase(repository = repository, context = context),
            getEmailUseCase = GetEmailUseCase(repository = repository),
            getNameUseCase = GetNameUseCase(repository = repository),
            getPhoneNumberUseCase = GetPhoneNumberUseCase(repository = repository),
            getPhotoUrlUseCase = GetPhotoUrlUseCase(repository = repository),
            getIdUseCase = GetIdUseCase(repository = repository),
            hasAccountSignedInUseCase = HasAccountSignedInUseCase(repository = repository),
            isEmailVerifiedUseCase = IsEmailVerifiedUseCase(repository = repository),
            isSignedInWithProviderUseCase = IsSignedInWithProviderUseCase(repository = repository),
            sendEmailVerificationUseCase = SendEmailVerificationUseCase(repository = repository),
            signInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(repository = repository, context = context),
            signInWithCredentialUseCase = SignInWithCredentialUseCase(repository = repository),
            updateProfileUseCase = UpdateProfileUseCase(repository = repository),
            updatePhoneNumberUseCase = UpdatePhoneNumberUseCase(repository = repository),
            uploadImageUseCase = UploadImageUseCase(repository = repository)
        )
    }
}