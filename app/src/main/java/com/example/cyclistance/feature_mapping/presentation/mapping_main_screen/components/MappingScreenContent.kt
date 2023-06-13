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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_dialogs.presentation.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogForegroundLocationPermission
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogPhonePermission
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.CancelledRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.banner.MappingExpandableBanner
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet.MappingBottomSheet
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons.RequestHelpButton
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons.RespondToHelpButton
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs.ExpandableFABSection
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs.FloatingButtonSection
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_request.RescueRequestDialog
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.mapboxsdk.maps.MapboxMap


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun MappingScreenContent(
    modifier: Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    mapboxMap: MapboxMap?,
    hasTransaction: Boolean = false,
    isRescueCancelled: Boolean = false,
    isNavigating: Boolean,
    uiState: MappingUiState,
    locationPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    event: (MappingUiEvent) -> Unit = {}
) {
    val respondentCount by remember(state.newRescueRequest?.request?.size) {
        derivedStateOf { (state.newRescueRequest?.request)?.size ?: 0 }
    }


    val configuration = LocalConfiguration.current

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {


        if (uiState.isRescueRequestDialogVisible) {
            RescueRequestDialog(
                modifier = Modifier
                    .fillMaxSize(),
                mappingState = state,
                uiState = uiState,
                event = event
            )
        }


        MappingBottomSheet(
            state = state,
            onClickRescueArrivedButton = { event(MappingUiEvent.RescueArrivedConfirmed) },
            onClickReachedDestinationButton = { event(MappingUiEvent.DestinationReachedConfirmed) },
            onClickCancelSearchButton = { event(MappingUiEvent.CancelSearchConfirmed) },
            onClickCallRescueTransactionButton = { event(MappingUiEvent.CallRescueTransaction) },
            onClickChatRescueTransactionButton = { event(MappingUiEvent.ChatRescueTransaction) },
            onClickCancelRescueTransactionButton = { event(MappingUiEvent.CancelRescueTransaction) },
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            bottomSheetType = uiState.bottomSheetType) {


            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (mapScreen, requestHelpButton, circularProgressbar, noInternetScreen, respondToHelpButton, fabSection, permissionDialog, expandableFabSection) = createRefs()


                MappingMapsScreen(
                    state = state,
                    modifier = Modifier.constrainAs(mapScreen) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    hasTransaction = hasTransaction,
                    isRescueCancelled = isRescueCancelled,
                    mapboxMap = mapboxMap,
                    routeDirection = uiState.routeDirection,
                    isNavigating = isNavigating,
                    event = event,
                    uiState = uiState
                )




                AnimatedVisibility(
                    visible = uiState.mapSelectedRescuee != null,
                    enter = expandVertically(expandFrom = Alignment.Top) { 20 },
                    exit = shrinkVertically(animationSpec = tween()) { fullHeight ->
                        fullHeight / 2
                    },
                ) {
                    if (uiState.mapSelectedRescuee != null) {
                        MappingExpandableBanner(
                            modifier = Modifier
                                .padding(all = 6.dp)
                                .fillMaxWidth(), banner = uiState.mapSelectedRescuee,
                            onClickDismissButton = { event(MappingUiEvent.DismissBanner) })
                    }
                }

                FloatingButtonSection(
                    modifier = Modifier
                        .constrainAs(fabSection) {
                            end.linkTo(parent.end, margin = 8.dp)
                            bottom.linkTo(
                                parent.bottom,
                                margin = (configuration.screenHeightDp / 2.5).dp)
                        },
                    locationPermissionGranted = locationPermissionState.allPermissionsGranted,
                    onClickLocateUserButton = { event(MappingUiEvent.LocateUser) },
                    onClickRouteOverviewButton = { event(MappingUiEvent.RouteOverview) },
                    onClickRecenterButton = { event(MappingUiEvent.RecenterRoute) },
                    onClickOpenNavigationButton = { event(MappingUiEvent.OpenNavigation) },
                    isNavigating = isNavigating,
                    uiState = uiState
                )



                ExpandableFABSection(
                    modifier = Modifier
                        .constrainAs(expandableFabSection) {
                            end.linkTo(parent.end, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 15.dp)
                        },
                    onClickEmergencyCall = { event(MappingUiEvent.OpenEmergencyCall) },
                    onClickFamilyTracker = { event(MappingUiEvent.OpenFamilyTracker) },
                    onClickRescueRequest = { event(MappingUiEvent.ShowRescueRequestDialog) },
                    onClickFab = { event(MappingUiEvent.OnToggleExpandableFAB) },
                    isFabExpanded = uiState.isFabExpanded,
                    badgeCount = respondentCount
                )

                RequestHelpButton(
                    modifier = Modifier.constrainAs(requestHelpButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }, onClickRequestHelpButton = { event(MappingUiEvent.RequestHelp) },
                    state = state,
                    visible = uiState.requestHelpButtonVisible && isNavigating.not()

                )

                RespondToHelpButton(
                    modifier = Modifier.constrainAs(respondToHelpButton) {
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    },
                    onClickRespondButton = { event(MappingUiEvent.RespondToHelp) },
                    state = state,
                    visible = uiState.requestHelpButtonVisible.not() && isNavigating.not()
                )

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.constrainAs(
                            circularProgressbar) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            this.centerTo(parent)
                        })
                }

                if (uiState.isNoInternetVisible) {
                    NoInternetDialog(
                        onDismiss = { event(MappingUiEvent.DismissNoInternetDialog) },
                        modifier = Modifier.constrainAs(noInternetScreen) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        })

                }

                if (uiState.locationPermissionDialogVisible) {
                    DialogForegroundLocationPermission(
                        modifier = Modifier.constrainAs(
                            permissionDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        }, onDismiss = { event(MappingUiEvent.DismissLocationPermission) }
                    )
                }

                if (uiState.phonePermissionDialogVisible) {
                    DialogPhonePermission(modifier = Modifier.constrainAs(permissionDialog) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                        centerTo(parent)
                    }, onDismiss = { event(MappingUiEvent.DismissPhonePermission) })
                }

                AnimatedVisibility(
                    visible = isRescueCancelled && uiState.rescueRequestAccepted.not(),
                    enter = fadeIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 220))) {

                    val rescueTransaction = state.rescueTransaction
                    val cancellation = rescueTransaction?.cancellation
                    val cancellationReason =
                        cancellation?.cancellationReason ?: return@AnimatedVisibility

                    RescueRequestCancelled(
                        modifier = Modifier.fillMaxSize(),
                        onClickOkButton = { event(MappingUiEvent.CancelledRescueConfirmed) },
                        cancelledRescueModel = CancelledRescueModel(
                            transactionID = rescueTransaction.id,
                            rescueCancelledBy = cancellation.nameCancelledBy,
                            reason = cancellationReason.reason,
                            message = cancellationReason.message
                        ))
                }

                AnimatedVisibility(
                    visible = uiState.rescueRequestAccepted && isRescueCancelled.not(),
                    enter = fadeIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 220))) {
                    RescueRequestAccepted(
                        modifier = Modifier.fillMaxSize(),
                        onClickOkButton = { event(MappingUiEvent.RescueRequestAccepted) },
                        acceptedName = state.rescuee?.name ?: "Name placeholder",
                    )
                }
            }

        }
    }
}
