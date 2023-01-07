package com.example.cyclistance.feature_mapping.domain.use_case

import com.example.cyclistance.feature_mapping.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.SetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.SetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.image.ImageUrlToDrawableUseCase
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

data class MappingUseCase(



    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val createUserUseCase: CreateUserUseCase,
    val confirmDetailsUseCase: ConfirmDetailsUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val deleteRescueRespondentUseCase: DeleteRescueRespondentUseCase,
    val addRescueRespondentUseCase: AddRescueRespondentUseCase,
    val deleteAllRespondentsUseCase: DeleteAllRespondentsUseCase,

    val getRescueTransactionByIdUseCase: GetRescueTransactionByIdUseCase,
    val createRescueTransactionUseCase: CreateRescueTransactionUseCase,
    val deleteRescueTransactionUseCase: DeleteRescueTransactionUseCase,
    val confirmCancellationUseCase: ConfirmCancellationUseCase,

    val getUserLocationUseCase: GetUserLocationUseCase,

    val imageUrlToDrawableUseCase: ImageUrlToDrawableUseCase,

    val getBikeTypeUseCase: GetBikeTypeUseCase,
    val setBikeTypeUseCase: SetBikeTypeUseCase,

    val getAddressUseCase: GetAddressUseCase,
    val setAddressUseCase: SetAddressUseCase,

    val broadcastRescueTransactionUseCase: BroadcastRescueTransactionUseCase,
    val broadcastUserUseCase: BroadcastUserUseCase,
    val broadcastTransactionLocationUseCase: BroadcastTransactionLocationUseCase,
    val getRescueTransactionUpdatesUseCase: GetRescueTransactionUpdatesUseCase,
    val getUserUpdatesUseCase: GetUserUpdatesUseCase,
    val getTransactionLocationUpdatesUseCase: GetTransactionLocationUpdatesUseCase,
    val getRouteDirectionsUseCase: GetRouteDirectionsUseCase

    )
