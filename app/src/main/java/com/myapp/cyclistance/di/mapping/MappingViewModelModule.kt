package com.myapp.cyclistance.di.mapping

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import com.myapp.cyclistance.feature_mapping.domain.use_case.MappingUseCase
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
            notificationIntentUseCase = NotificationIntentUseCase(mappingUiStoreRepository),
            rescueFinishUseCase = RescueFinishUseCase(mappingRepository),
            defaultMapTypeUseCase = DefaultMapTypeUseCase(mappingUiStoreRepository),
            hazardousMapTypeUseCase = HazardousMapTypeUseCase(mappingUiStoreRepository),
            trafficMapTypeUseCase = TrafficMapTypeUseCase(mappingUiStoreRepository),
            removeUserTransactionUseCase = RemoveUserTransactionUseCase(mappingRepository)

        )

    }

}