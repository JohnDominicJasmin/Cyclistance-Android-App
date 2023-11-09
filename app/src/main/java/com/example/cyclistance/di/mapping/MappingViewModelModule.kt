package com.example.cyclistance.di.mapping

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named


@Module
@InstallIn(ViewModelComponent::class)
object MappingViewModelModule {


    @Provides
    @ViewModelScoped
    fun provideMappingUseCase(
        mappingRepository: MappingRepository,
        mappingUiStoreRepository: MappingUiStoreRepository,
        mappingSocketRepository: MappingSocketRepository,
        notificationManagerCompat: NotificationManagerCompat,
        @Named("newRescueNotification") newRescueNotification: NotificationCompat.Builder,
        @Named("requestAcceptedNotification") requestAcceptedNotification: NotificationCompat.Builder,

    ): MappingUseCase {
        return MappingUseCase(

            getUserByIdUseCase = GetUserByIdUseCase(mappingRepository),
            createUserUseCase = CreateUserUseCase(mappingRepository),
            deleteUserUseCase = DeleteUserUseCase(mappingRepository),

            getRescueTransactionByIdUseCase = GetRescueTransactionByIdUseCase(mappingRepository),
            acceptRescueRequestUseCase = AcceptRescueRequestUseCase(mappingRepository),
            deleteRescueTransactionUseCase = DeleteRescueTransactionUseCase(mappingRepository),


            getUserLocationUseCase = GetUserLocationUseCase(mappingRepository),
            getFullAddressUseCase = GetFullAddressUseCase(mappingRepository),

            bikeTypeUseCase = BikeTypeUseCase(mappingUiStoreRepository),
            addressUseCase = AddressUseCase(mappingUiStoreRepository),
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(
                mappingSocketRepository),
            nearbyCyclistsUseCase = NearbyCyclistsUseCase(mappingSocketRepository),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(
                mappingSocketRepository),
            transactionLocationUseCase = TransactionLocationUseCase(
                mappingSocketRepository),
            deleteRescueRespondentUseCase = DeleteRescueRespondentUseCase(mappingRepository),
            addRescueRespondentUseCase = AddRescueRespondentUseCase(mappingRepository),
            deleteAllRespondentsUseCase = DeleteAllRespondentsUseCase(mappingRepository),
            confirmDetailsUseCase = ConfirmDetailsUseCase(mappingRepository),
            confirmCancellationUseCase = ConfirmCancellationUseCase(mappingRepository),
            getRouteDirectionsUseCase = GetRouteDirectionsUseCase(mappingRepository),
            getCalculatedDistanceUseCase = GetCalculatedDistanceUseCase(mappingRepository),
            bottomSheetTypeUseCase = BottomSheetTypeUseCase(mappingUiStoreRepository),
            newHazardousLaneUseCase = NewHazardousLaneUseCase(mappingRepository),
            deleteHazardousLaneUseCase = DeleteHazardousLaneUseCase(mappingRepository),
            removeHazardousListenerUseCase = RemoveHazardousListenerUseCase(mappingRepository),
            updateHazardousLaneUseCase = UpdateHazardousLaneUseCase(mappingRepository),
            shouldHazardousStartingInfoUseCase = ShouldHazardousStartingInfoUseCase(
                mappingUiStoreRepository),
            newRescueRequestNotificationUseCase = NewRescueRequestNotificationUseCase(
                notificationManagerCompat = notificationManagerCompat,
                notificationBuilder = newRescueNotification
            ),
            requestAcceptedNotificationUseCase = RequestAcceptedNotificationUseCase(
                notificationManagerCompat = notificationManagerCompat,
                notificationBuilder = requestAcceptedNotification
            ),
            cancelHelpRespondUseCase = CancelHelpRespondUseCase(mappingRepository),
            intentActionUseCase = IntentActionUseCase(mappingUiStoreRepository),
            rescueFinishUseCase = RescueFinishUseCase(mappingRepository),
            defaultMapTypeUseCase = DefaultMapTypeUseCase(mappingUiStoreRepository),
            hazardousMapTypeUseCase = HazardousMapTypeUseCase(mappingUiStoreRepository),
            trafficMapTypeUseCase = TrafficMapTypeUseCase(mappingUiStoreRepository),

        )

    }

}