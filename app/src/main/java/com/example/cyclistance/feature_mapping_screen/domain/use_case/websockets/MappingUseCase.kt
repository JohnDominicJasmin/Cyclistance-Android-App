package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets

import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.UpdateAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.UpdateBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.image.ImageUrlToDrawableUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.CreateRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.CreateUserUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.DeleteUserUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location.BroadcastTransactionLocationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location.GetTransactionLocationUpdatesUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.users.BroadcastUserUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.users.GetUserUpdatesUseCase

data class MappingUseCase(



    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val createUserUseCase: CreateUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,

    val getRescueTransactionByIdUseCase: GetRescueTransactionByIdUseCase,
    val createRescueTransactionUseCase: CreateRescueTransactionUseCase,
    val deleteRescueTransactionUseCase: DeleteRescueTransactionUseCase,

    val getUserLocationUseCase: GetUserLocationUseCase,

    val imageUrlToDrawableUseCase: ImageUrlToDrawableUseCase,

    val getBikeTypeUseCase: GetBikeTypeUseCase,
    val updateBikeTypeUseCase: UpdateBikeTypeUseCase,

    val getAddressUseCase: GetAddressUseCase,
    val updateAddressUseCase: UpdateAddressUseCase,

    val broadcastRescueTransactionUseCase: BroadcastRescueTransactionUseCase,
    val broadcastUserUseCase: BroadcastUserUseCase,
    val broadcastTransactionLocationUseCase: BroadcastTransactionLocationUseCase,
    val getRescueTransactionUpdatesUseCase: GetRescueTransactionUpdatesUseCase,
    val getUserUpdatesUseCase: GetUserUpdatesUseCase,
    val getTransactionLocationUpdatesUseCase: GetTransactionLocationUpdatesUseCase

    )
