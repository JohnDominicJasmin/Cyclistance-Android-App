package com.example.cyclistance.di

import com.example.cyclistance.feature_mapping.data.repositories.FakeMappingRepository
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.AddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.BikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.AcceptRescueRequestUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.AddRescueRespondentUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.ConfirmDetailsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.CreateUserUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteAllRespondentsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteRescueRespondentUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteUserUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.TransactionLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.NearbyCyclistsUseCase

object TestMappingModule {
    private val fakeMappingRepository: FakeMappingRepository = FakeMappingRepository()

    operator fun invoke() = FakeMappingRepository.Companion
    fun provideTestMappingUseCase(): MappingUseCase {
        val repository = fakeMappingRepository
        return MappingUseCase(
            getUsersUseCase = GetUsersUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            createUserUseCase = CreateUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),

            getRescueTransactionByIdUseCase = GetRescueTransactionByIdUseCase(repository),
            acceptRescueRequestUseCase = AcceptRescueRequestUseCase(repository),
            deleteRescueTransactionUseCase = DeleteRescueTransactionUseCase(repository),


            getUserLocationUseCase = GetUserLocationUseCase(repository),
            getFullAddressUseCase = GetFullAddressUseCase(repository),
            bikeTypeUseCase = BikeTypeUseCase(repository),
            setBikeTypeUseCase = SetBikeTypeUseCase(repository),
            addressUseCase = AddressUseCase(repository),
            setAddressUseCase = SetAddressUseCase(repository),
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(repository),
            nearbyCyclistsUseCase = NearbyCyclistsUseCase(repository),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(repository),
            getUserUpdatesUseCase = GetUserUpdatesUseCase(repository),
            transactionLocationUseCase = TransactionLocationUseCase(repository),
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






}