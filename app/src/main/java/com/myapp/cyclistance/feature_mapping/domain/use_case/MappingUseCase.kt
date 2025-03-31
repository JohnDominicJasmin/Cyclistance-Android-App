package com.myapp.cyclistance.feature_mapping.domain.use_case

import com.myapp.cyclistance.feature_mapping.domain.use_case.address.AddressUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.bike_type.BikeTypeUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type.BottomSheetTypeUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane.DeleteHazardousLaneUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane.NewHazardousLaneUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane.RemoveHazardousListenerUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane.ShouldHazardousStartingInfoUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane.UpdateHazardousLaneUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.intent_action.NotificationIntentUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.map_type.DefaultMapTypeUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.map_type.HazardousMapTypeUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.map_type.TrafficMapTypeUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.notification.NewRescueRequestNotificationUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.notification.RequestAcceptedNotificationUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.AcceptRescueRequestUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.CancelHelpRequestUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.RemoveUserTransactionUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.rescue_transaction.RescueFinishUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.AddRescueRespondentUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.CancelHelpRespondUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.ConfirmDetailsUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.CreateUserUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.DeleteAllRespondentsUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.DeleteRescueRespondentUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.DeleteUserUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.user.GetUserByIdUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.live_location.TransactionLocationUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.myapp.cyclistance.feature_mapping.domain.use_case.websockets.users.NearbyCyclistsUseCase

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
    val removeUserTransactionUseCase: RemoveUserTransactionUseCase,
    val cancelHelpRequestUseCase: CancelHelpRequestUseCase,

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
    val notificationIntentUseCase: NotificationIntentUseCase
    )
