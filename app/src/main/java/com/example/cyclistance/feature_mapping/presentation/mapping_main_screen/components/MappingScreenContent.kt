package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_mapping.domain.model.CancelledRescueModel
import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.RouteDirection
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet.MappingBottomSheet
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs.FloatingButtonSection
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun MappingScreenContent(
    modifier: Modifier,
    isDarkTheme: Boolean,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    mapboxMap: MapboxMap?,
    hasTransaction: Boolean = false,
    bottomSheetType: String,
    isRescueCancelled: Boolean = false,
    mapSelectedRescuee: MapSelectedRescuee?,
    routeDirection: RouteDirection?,
    isNavigating: Boolean,
    isNoInternetDialogVisible: Boolean,
    rescueRequestAccepted: Boolean,
    requestHelpButtonVisible: Boolean,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    onClickRequestHelpButton: () -> Unit = {},
    onClickRespondToHelpButton: () -> Unit = {},
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    onClickOkButtonCancelledRescue: () -> Unit = {},
    onInitializeMapboxMap: (MapboxMap) -> Unit = {},
    onClickOkButtonRescueRequestAccepted: () -> Unit = {},
    onChangeCameraState: (LatLng, Double) -> Unit = { _, _ -> },
    onDismissNoInternetDialog: () -> Unit = {},
    onClickRescueeMapIcon: (String) -> Unit = {},
    onMapClick: () -> Unit = {},
    onClickDismissBannerButton: () -> Unit = {},
    onClickLocateUserButton: () -> Unit = {},
    onClickRouteOverButton: () -> Unit = {},
    onClickRecenterButton: () -> Unit = {},
    onClickOpenNavigationButton: () -> Unit = {},
    onRequestNavigationCameraToOverview: () -> Unit = {},
) {

    val configuration = LocalConfiguration.current



    Surface(modifier = modifier.fillMaxSize()) {

        MappingBottomSheet(
            isDarkTheme = isDarkTheme,
            state = state,
            onClickRescueArrivedButton = onClickRescueArrivedButton,
            onClickReachedDestinationButton = onClickReachedDestinationButton,
            onClickCancelSearchButton = onClickCancelSearchButton,
            onClickCallRescueTransactionButton = onClickCallRescueTransactionButton,
            onClickChatRescueTransactionButton = onClickChatRescueTransactionButton,
            onClickCancelRescueTransactionButton = onClickCancelRescueTransactionButton,
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            bottomSheetType = bottomSheetType,
        ) {

            ConstraintLayout {

                val (mapScreen, requestHelpButton, circularProgressbar, noInternetScreen, respondToHelpButton, floatingButtonSection) = createRefs()


                MappingMapsScreen(
                    state = state,
                    modifier = Modifier.constrainAs(mapScreen) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    isDarkTheme = isDarkTheme,
                    onInitializeMapboxMap = onInitializeMapboxMap,
                    onChangeCameraState = onChangeCameraState,
                    hasTransaction = hasTransaction,
                    isRescueCancelled = isRescueCancelled,
                    onClickRescueeMapIcon = onClickRescueeMapIcon,
                    onMapClick = onMapClick,
                    requestNavigationCameraToOverview = onRequestNavigationCameraToOverview,
                    mapboxMap = mapboxMap,
                    routeDirection = routeDirection,
                    isNavigating = isNavigating
                )




                AnimatedVisibility(
                    visible = mapSelectedRescuee != null,
                    enter = expandVertically(expandFrom = Alignment.Top) { 20 },
                    exit = shrinkVertically(animationSpec = tween()) { fullHeight ->
                        fullHeight / 2
                    },
                ) {
                    if (mapSelectedRescuee != null) {
                        MappingExpandableBanner(
                            modifier = Modifier
                                .padding(all = 6.dp)
                                .fillMaxWidth(), banner = mapSelectedRescuee,
                            onClickDismissButton = onClickDismissBannerButton)
                    }
                }

                FloatingButtonSection(
                    modifier = Modifier
                        .constrainAs(floatingButtonSection) {
                            end.linkTo(parent.end, margin = 4.dp)
                            bottom.linkTo(
                                parent.bottom,
                                margin = (configuration.screenHeightDp / 3).dp)
                        },
                    locationPermissionGranted = locationPermissionState.allPermissionsGranted,
                    onClickLocateUserButton = onClickLocateUserButton,
                    onClickRouteOverButton = onClickRouteOverButton,
                    onClickRecenterButton = onClickRecenterButton,
                    onClickOpenNavigationButton = onClickOpenNavigationButton,
                    isNavigating = isNavigating
                )

                RequestHelpButton(
                    modifier = Modifier.constrainAs(requestHelpButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }, onClickSearchButton = onClickRequestHelpButton,
                    state = state,
                    visible = requestHelpButtonVisible && isNavigating.not()

                )

                RespondToHelpButton(
                    modifier = Modifier.constrainAs(respondToHelpButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    },
                    onClickRespondButton = onClickRespondToHelpButton,
                    state = state,
                    visible = requestHelpButtonVisible.not() && isNavigating.not()
                )

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(circularProgressbar) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        this.centerTo(parent)
                    })
                }

                if (isNoInternetDialogVisible) {
                    NoInternetDialog(
                        onDismiss = onDismissNoInternetDialog,
                        modifier = Modifier.constrainAs(noInternetScreen) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        })

                }

                AnimatedVisibility(
                    visible = isRescueCancelled && rescueRequestAccepted.not(),
                    enter = fadeIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 220))) {

                    val rescueTransaction = state.rescueTransaction
                    val cancellation = rescueTransaction?.cancellation
                    val cancellationReason =
                        cancellation?.cancellationReason ?: return@AnimatedVisibility


                    MappingCancelledRescue(
                        modifier = Modifier.fillMaxSize(),
                        onClickOkButton = onClickOkButtonCancelledRescue,
                        cancelledRescueModel = CancelledRescueModel(
                            transactionID = rescueTransaction.id,
                            rescueCancelledBy = cancellation.nameCancelledBy,
                            reason = cancellationReason.reason,
                            message = cancellationReason.message
                        ))
                }

                AnimatedVisibility(
                    visible = rescueRequestAccepted && isRescueCancelled.not(),
                    enter = fadeIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 220))) {
                    RescueRequestAccepted(
                        modifier = Modifier.fillMaxSize(),
                        onClickOkButton = onClickOkButtonRescueRequestAccepted,
                        acceptedName = state.rescuee?.name ?: "Name placeholder",
                    )
                }
            }

        }
    }
}
