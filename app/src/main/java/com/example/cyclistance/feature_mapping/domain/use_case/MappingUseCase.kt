package com.example.cyclistance.feature_mapping.domain.use_case

import com.example.cyclistance.feature_mapping.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.SetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.SetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type.GetBottomSheetTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type.SetBottomSheetTypeUseCase
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
import com.example.cyclistance.feature_mapping.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane.NewHazardousLaneUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.TransactionLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.NearbyCyclistsUseCase

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
    val acceptRescueRequestUseCase: AcceptRescueRequestUseCase,
    val deleteRescueTransactionUseCase: DeleteRescueTransactionUseCase,
    val confirmCancellationUseCase: ConfirmCancellationUseCase,

    val getUserLocationUseCase: GetUserLocationUseCase,
    val getFullAddressUseCase: GetFullAddressUseCase,


    val getBikeTypeUseCase: GetBikeTypeUseCase,
    val setBikeTypeUseCase: SetBikeTypeUseCase,

    val getAddressUseCase: GetAddressUseCase,
    val setAddressUseCase: SetAddressUseCase,

    val getBottomSheetTypeUseCase: GetBottomSheetTypeUseCase,
    val setBottomSheetTypeUseCase: SetBottomSheetTypeUseCase,

    val broadcastRescueTransactionUseCase: BroadcastRescueTransactionUseCase,
    val nearbyCyclistsUseCase: NearbyCyclistsUseCase,
    val transactionLocationUseCase: TransactionLocationUseCase,
    val getRescueTransactionUpdatesUseCase: GetRescueTransactionUpdatesUseCase,
    val getRouteDirectionsUseCase: GetRouteDirectionsUseCase,
    val getCalculatedDistanceUseCase: GetCalculatedDistanceUseCase,
    val newHazardousLaneUseCase: NewHazardousLaneUseCase
    )
