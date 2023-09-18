package com.example.cyclistance.di.mapping

import android.content.Context
import android.location.Geocoder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cyclistance.feature_mapping.data.CyclistanceApi
import com.example.cyclistance.feature_mapping.data.repository.MappingRepositoryImpl
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
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.map_type.MapTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.notification.ShowNotificationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.AcceptRescueRequestUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.AddRescueRespondentUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.CancelHelpRespondUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.ConfirmDetailsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.CreateUserUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteAllRespondentsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteRescueRespondentUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.DeleteUserUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.TransactionLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.NearbyCyclistsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.api.optimization.v1.MapboxOptimization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named


@Module
@InstallIn(ViewModelComponent::class)
object MappingViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideMappingRepository(
        @ApplicationContext context: Context,
        api: CyclistanceApi,
        fireStore: FirebaseFirestore,
        mapboxDirections: MapboxOptimization.Builder): MappingRepository {


        val geocoder = Geocoder(context)

        return MappingRepositoryImpl(
            api = api,
            context = context,
            mapboxDirections = mapboxDirections,
            geocoder = geocoder,
            fireStore = fireStore
        )
    }





    @Provides
    @ViewModelScoped
    fun provideMappingUseCase(
        mappingRepository: MappingRepository,
        mappingUiStoreRepository: MappingUiStoreRepository,
        mappingSocketRepository: MappingSocketRepository,
        notificationManagerCompat: NotificationManagerCompat,
        @Named("rescueNotification")  notificationBuilder: NotificationCompat.Builder
        ): MappingUseCase {
        return MappingUseCase(

            getUsersUseCase = GetUsersUseCase(mappingRepository),
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
            mapTypeUseCase = MapTypeUseCase(mappingUiStoreRepository),
            removeHazardousListenerUseCase = RemoveHazardousListenerUseCase(mappingRepository),
            updateHazardousLaneUseCase = UpdateHazardousLaneUseCase(mappingRepository),
            shouldHazardousStartingInfoUseCase = ShouldHazardousStartingInfoUseCase(
                mappingUiStoreRepository),
            showNotificationUseCase = ShowNotificationUseCase(
                notificationManagerCompat = notificationManagerCompat,
                notificationBuilder = notificationBuilder
            ),
            cancelHelpRespondUseCase = CancelHelpRespondUseCase(mappingRepository)
        )

    }

}