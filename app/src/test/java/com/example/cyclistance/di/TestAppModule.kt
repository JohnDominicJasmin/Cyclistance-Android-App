package com.example.cyclistance.di

import com.example.cyclistance.feature_authentication.data.repositories.FakeAuthRepository
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.create_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.read_account.*
import com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account.SignOutUseCase
import com.example.cyclistance.feature_authentication.domain.use_case.verify_account.*
import com.example.cyclistance.feature_mapping.data.repositories.FakeMappingRepository
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.SetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.SetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
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

object TestAppModule {


    fun provideTestMappingUseCase(): MappingUseCase {
        val repository = FakeMappingRepository()
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
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(repository),
            broadcastUserUseCase = BroadcastUserUseCase(repository),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(repository),
            getUserUpdatesUseCase = GetUserUpdatesUseCase(repository),
            broadcastTransactionLocationUseCase = BroadcastTransactionLocationUseCase(repository),
            getTransactionLocationUpdatesUseCase = GetTransactionLocationUpdatesUseCase(repository),
            deleteRescueRespondentUseCase = DeleteRescueRespondentUseCase(repository),
            addRescueRespondentUseCase = AddRescueRespondentUseCase(repository),
            deleteAllRespondentsUseCase = DeleteAllRespondentsUseCase(repository),
            confirmDetailsUseCase = ConfirmDetailsUseCase(repository),
            confirmCancellationUseCase = ConfirmCancellationUseCase(repository),
            getRouteDirectionsUseCase = GetRouteDirectionsUseCase(repository),
            getCalculatedDistanceUseCase = GetCalculatedDistanceUseCase(repository),
        )
    }



    fun provideTestAuthUseCase(): AuthenticationUseCase {
        val repository = FakeAuthRepository()
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