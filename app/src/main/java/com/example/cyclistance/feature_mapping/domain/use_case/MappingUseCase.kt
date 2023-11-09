package com.example.cyclistance.feature_mapping.domain.use_case

import com.example.cyclistance.feature_mapping.domain.use_case.address.AddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.BikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type.BottomSheetTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane.DeleteHazardousLaneUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane.NewHazardousLaneUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane.RemoveHazardousListenerUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane.ShouldHazardousStartingInfoUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane.UpdateHazardousLaneUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.intent_action.IntentActionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.map_type.DefaultMapTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.map_type.HazardousMapTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.map_type.TrafficMapTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.notification.NewRescueRequestNotificationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.notification.RequestAcceptedNotificationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.AcceptRescueRequestUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.RescueFinishUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.AddRescueRespondentUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.CancelHelpRespondUseCase
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

data class MappingUseCase(



    val getUserByIdUseCase: GetUserByIdUseCase,
    val createUserUseCase: CreateUserUseCase,
    val confirmDetailsUseCase: ConfirmDetailsUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val deleteRescueRespondentUseCase: DeleteRescueRespondentUseCase,
    val addRescueRespondentUseCase: AddRescueRespondentUseCase,
    val deleteAllRespondentsUseCase: DeleteAllRespondentsUseCase,
    val cancelHelpRespondUseCase: CancelHelpRespondUseCase,

    val getRescueTransactionByIdUseCase: GetRescueTransactionByIdUseCase,
    val acceptRescueRequestUseCase: AcceptRescueRequestUseCase,
    val deleteRescueTransactionUseCase: DeleteRescueTransactionUseCase,
    val confirmCancellationUseCase: ConfirmCancellationUseCase,
    val rescueFinishUseCase: RescueFinishUseCase,

    val getUserLocationUseCase: GetUserLocationUseCase,
    val getFullAddressUseCase: GetFullAddressUseCase,


    val bikeTypeUseCase: BikeTypeUseCase,
    val addressUseCase: AddressUseCase,
    val bottomSheetTypeUseCase: BottomSheetTypeUseCase,

    val broadcastRescueTransactionUseCase: BroadcastRescueTransactionUseCase,
    val nearbyCyclistsUseCase: NearbyCyclistsUseCase,
    val transactionLocationUseCase: TransactionLocationUseCase,
    val getRescueTransactionUpdatesUseCase: GetRescueTransactionUpdatesUseCase,
    val getRouteDirectionsUseCase: GetRouteDirectionsUseCase,
    val getCalculatedDistanceUseCase: GetCalculatedDistanceUseCase,
    val newHazardousLaneUseCase: NewHazardousLaneUseCase,
    val removeHazardousListenerUseCase: RemoveHazardousListenerUseCase,
    val updateHazardousLaneUseCase: UpdateHazardousLaneUseCase,
    val deleteHazardousLaneUseCase: DeleteHazardousLaneUseCase,

    val shouldHazardousStartingInfoUseCase: ShouldHazardousStartingInfoUseCase,

    val defaultMapTypeUseCase: DefaultMapTypeUseCase,
    val hazardousMapTypeUseCase: HazardousMapTypeUseCase,
    val trafficMapTypeUseCase: TrafficMapTypeUseCase,

    val newRescueRequestNotificationUseCase: NewRescueRequestNotificationUseCase,
    val requestAcceptedNotificationUseCase: RequestAcceptedNotificationUseCase,
    val intentActionUseCase: IntentActionUseCase
    )
