package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.myapp.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogBackgroundLocationPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogCameraPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogFilesAndMediaPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogForegroundLocationPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogNotificationPermission
import com.myapp.cyclistance.core.presentation.dialogs.permissions_dialog.DialogPhonePermission
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessBackgroundLocationDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessCameraDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessForegroundLocationDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessGalleryDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessNotificationDialog
import com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog.AccessPhoneCallDialog
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.core.utils.formatter.IconFormatter.toHazardousLaneIconMarker
import com.myapp.cyclistance.feature_authentication.presentation.common.visible
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call.EmergencyCallDialog
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.CancelledRescueModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.banner.MappingExpandableBanner
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.MappingBottomSheet
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons.CancelRespondButton
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons.RequestHelpButton
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons.RespondToHelpButton
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.AccessPhotoDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.BannedAccountDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.CancelOnGoingRescueDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.CancelSearchDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.DeleteHazardousLaneMarkerDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.DiscardHazardousLaneMarkerDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description.IncidentDescriptionDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.report_incident.ReportIncidentDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs.ExpandableFABSection
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs.FloatingButtonSection
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.request_bottom_dialog.MappingRequestAccepted
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.request_bottom_dialog.MappingRequestCancelled
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_request.RescueRequestDialog
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingScreenContent(
    modifier: Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    emergencyState: EmergencyCallState,
    mapboxMap: MapboxMap?,
    uiState: MappingUiState,
    incidentDescription: TextFieldValue,
    hazardousLaneMarkers: List<HazardousLaneMarkerDetails>,
    locationPermissionGranted: Boolean,
    event: (MappingUiEvent) -> Unit = {},
) {

    val respondentCount by remember(state.newRescueRequest?.request?.size) {
        derivedStateOf { (state.newRescueRequest?.request)?.size ?: 0 }
    }

    var lastNotifiedRequestId by rememberSaveable { mutableStateOf("") }
    var lastNotifiedAcceptedId by rememberSaveable { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val markerPostedCount by remember(hazardousLaneMarkers.size) {
        derivedStateOf {
            hazardousLaneMarkers.count { it.idCreator == state.userId }
        }
    }

    LaunchedEffect(key1 = respondentCount) {
        val request = state.newRescueRequest?.request?.lastOrNull() ?: return@LaunchedEffect
        if (lastNotifiedRequestId == request.id) {
            return@LaunchedEffect
        }

        event(MappingUiEvent.NotifyNewRescueRequest(message = "Request from ${request.name}, distance is ${request.distance}"))
        lastNotifiedRequestId = request.id ?: ""
    }

    LaunchedEffect(key1 = uiState.rescueRequestAccepted, key2 = uiState.isRescueCancelled.not()) {


        val rescueeId = state.rescuee?.id
        if (lastNotifiedAcceptedId == rescueeId) {
            return@LaunchedEffect
        }
        if (uiState.rescueRequestAccepted && uiState.isRescueCancelled.not()) {

            event(
                MappingUiEvent.NotifyRequestAccepted(
                    message = "${state.rescuee?.name} accepted your request"))
            lastNotifiedAcceptedId = rescueeId ?: ""
        }
    }


    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background) {


        Box {

            if (uiState.isRescueRequestDialogVisible) {
                RescueRequestDialog(
                    modifier = Modifier
                        .fillMaxSize(),
                    mappingState = state,
                    uiState = uiState,
                    event = event
                )
            }

            if (uiState.callPhonePermissionDialogVisible) {
                DialogPhonePermission(
                    modifier = Modifier,
                    onDismiss = {
                        event(MappingUiEvent.DismissCallPhonePermissionDialog)
                    }
                )
            }

            MappingBottomSheet(
                state = state,
                event = event,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                uiState = uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .align(Alignment.BottomCenter)) {


                ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                    val (mapScreen, requestHelpButton, circularProgressbar, dialog, respondToHelpButton, fabSection, expandableFabSection) = createRefs()


                    MappingMapsScreen(
                        state = state,
                        modifier = Modifier.constrainAs(mapScreen) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        mapboxMap = mapboxMap,
                        event = event,
                        uiState = uiState,
                        hazardousLaneMarkers = hazardousLaneMarkers
                    )




                    AnimatedVisibility(
                        visible = uiState.mapSelectedRescuee != null && bottomSheetScaffoldState.bottomSheetState.isCollapsed,
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
                                top.linkTo(parent.top, margin = 15.dp)
                                height = Dimension.fillToConstraints

                            },
                        locationPermissionGranted = locationPermissionGranted,
                        onClickLocateUserButton = { event(MappingUiEvent.LocateUser) },
                        onClickRouteOverviewButton = { event(MappingUiEvent.RouteOverview) },
                        onClickRecenterButton = { event(MappingUiEvent.RecenterRoute) },
                        onClickOpenNavigationButton = { event(MappingUiEvent.OpenNavigation) },
                        onClickLayerButton = {
                            val mapTypeBottomSheetVisibility =
                                bottomSheetScaffoldState.bottomSheetState.isExpanded && uiState.bottomSheetType == BottomSheetType.MapType.type
                            event(MappingUiEvent.MapTypeBottomSheet(visibility = !mapTypeBottomSheetVisibility))
                        },
                        uiState = uiState
                    )

                    ExpandableFABSection(
                        onClickEmergencyCall = { event(MappingUiEvent.EmergencyCallDialog(visibility = true)) },
                        onClickFamilyTracker = { event(MappingUiEvent.OpenFamilyTracker) },
                        onClickRescueRequest = { event(MappingUiEvent.RescueRequestDialog(visibility = true)) },
                        onClickFab = { event(MappingUiEvent.ExpandableFab(expanded = !uiState.isFabExpanded)) },
                        onClickBikeTracker = { event(MappingUiEvent.OpenSinoTrack) },
                        isFabExpanded = uiState.isFabExpanded,
                        badgeCount = respondentCount,
                        modifier = Modifier.constrainAs(expandableFabSection) {
                            end.linkTo(parent.end, margin = 8.dp)
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) bottom.linkTo(
                                parent.bottom,
                                margin = 15.dp) else top.linkTo(fabSection.bottom, margin = 8.dp)
                        }
                    )


                    val buttonVisible =
                        uiState.isNavigating.not() && uiState.isFabExpanded.not() && bottomSheetScaffoldState.bottomSheetState.isCollapsed
                    val requestHelpVisible = uiState.requestHelpButtonVisible && buttonVisible
                    val respondToHelpVisible =
                        uiState.requestHelpButtonVisible.not() && buttonVisible

                    val requestPending =
                        state.user.isRescueRequestPending(uiState.mapSelectedRescuee?.userId) == true

                    RequestHelpButton(
                        modifier = Modifier.constrainAs(requestHelpButton) {
                            bottom.linkTo(parent.bottom, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }, onClickRequestHelpButton = { event(MappingUiEvent.RequestHelp) },
                        state = state,
                        visible = requestHelpVisible

                    )

                    RespondToHelpButton(
                        modifier = Modifier.constrainAs(respondToHelpButton) {
                            bottom.linkTo(parent.bottom, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        },
                        onClickRespondButton = { event(MappingUiEvent.RespondToHelp) },
                        state = state,
                        visible = respondToHelpVisible && !requestPending
                    )


                    CancelRespondButton(
                        cancelRespond = { event(MappingUiEvent.CancelRespondHelp) },
                        modifier = Modifier.constrainAs(respondToHelpButton) {
                            bottom.linkTo(parent.bottom, margin = 15.dp)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        },
                        state = state,
                        visible = respondToHelpVisible && requestPending)



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

                    if (uiState.reportIncidentDialogVisible) {
                        ReportIncidentDialog(
                            incidentDescription = incidentDescription,
                            onNewLabel = {
                                event(MappingUiEvent.OnChangeIncidentLabel(it))
                            },
                            markerPostedCount = markerPostedCount,
                            uiState = uiState,
                            onChangeDescription = {
                                event(MappingUiEvent.OnChangeIncidentDescription(it))
                            },
                            addIncidentImage = { event(MappingUiEvent.AccessPhotoDialog(visibility = true)) },
                            viewImage = { event(MappingUiEvent.ViewImage) },
                            onClickConfirm = {
                                event(MappingUiEvent.OnReportIncident(uiState.selectedIncidentLabel))
                            }, onDismissRequest = {
                                event(MappingUiEvent.ReportIncidentDialog(visibility = false))
                                event(MappingUiEvent.ResetIncidentReport)

                            })
                    }


                    if (uiState.incidentDescriptionDialogVisible) {
                        IncidentDescriptionDialog(
                            uiState = uiState,
                            state = state,
                            icon = uiState.selectedHazardousMarker!!.label.toHazardousLaneIconMarker(),
                            onClickEdit = {
                                event(
                                    MappingUiEvent.OnClickEditIncidentDescription(
                                        uiState.selectedHazardousMarker))
                            },
                            onClickDelete = { event(MappingUiEvent.OnClickDeleteIncident) },
                            viewProofIncident = { event(MappingUiEvent.ViewImageIncidentDetails) },
                            onClickCancelButton = { event(MappingUiEvent.CancelEditIncidentDescription) },
                            onClickGotItButton = { event(MappingUiEvent.OnClickHazardousInfoGotIt) },
                            onDismissRequest = {
                                event(MappingUiEvent.IncidentDescriptionDialog(visibility = false))
                                event(MappingUiEvent.ResetIncidentReport)
                            },
                            onClickConfirmButton = { description, label ->
                                event(
                                    MappingUiEvent.UpdateIncidentDescription(
                                        label = label,
                                        description = description))
                            })
                    }

                    if (uiState.cancelSearchDialogVisible) {
                        CancelSearchDialog(onDismissRequest = {
                            event(MappingUiEvent.CancelSearchDialog(visibility = false))
                        }, onClickOkay = {
                            event(MappingUiEvent.SearchCancelled)
                        }, modifier = Modifier.constrainAs(dialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                            this.centerTo(parent)
                        })
                    }

                    if (uiState.cancelOnGoingRescueDialogVisible) {
                        CancelOnGoingRescueDialog(
                            onDismissRequest = {
                                event(MappingUiEvent.CancelOnGoingRescueDialog(visibility = false))
                            },
                            onClickOkay = { event(MappingUiEvent.CancelOnGoingRescue) },
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.matchParent
                                height = Dimension.wrapContent
                                this.centerTo(parent)
                            })
                    }


                    if(uiState.prominentBackgroundLocationDialogVisible){
                        AccessBackgroundLocationDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.DismissProminentBackgroundLocationDialog)
                            },
                            onDeny = {
                                event(MappingUiEvent.DismissProminentBackgroundLocationDialog)
                            },
                            onAllow = {
                                event(MappingUiEvent.DismissProminentBackgroundLocationDialog)
                                event(MappingUiEvent.AllowProminentBackgroundLocationDialog)
                            })
                    }



                    if (uiState.isEmergencyCallDialogVisible) {
                        EmergencyCallDialog(
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.matchParent
                                height = Dimension.wrapContent
                                this.centerTo(parent)
                            },
                            onDismiss = { event(MappingUiEvent.EmergencyCallDialog(visibility = false)) },
                            emergencyCallModel = emergencyState.emergencyCallModel,
                            onClick = {
                                event(
                                    MappingUiEvent.OnEmergencyCall(
                                        it.phoneNumber
                                    )
                                )
                            }, onAddContact = {
                                event(MappingUiEvent.OnAddEmergencyContact)
                                event(MappingUiEvent.EmergencyCallDialog(visibility = false))
                            }

                        )
                    }

                    if (uiState.deleteHazardousMarkerDialogVisible) {
                        DeleteHazardousLaneMarkerDialog(
                            onDismissRequest = {
                                event(
                                    MappingUiEvent.HazardousLaneMarkerDialog(
                                        visibility = false))
                            },
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.matchParent
                                height = Dimension.wrapContent
                                this.centerTo(parent)
                            },
                            onClickConfirmButton = {
                                event(MappingUiEvent.OnConfirmDeleteIncident)
                                event(MappingUiEvent.HazardousLaneMarkerDialog(visibility = false))
                            })
                    }

                    if (uiState.isNoInternetVisible) {
                        NoInternetDialog(
                            onDismiss = { event(MappingUiEvent.NoInternetDialog(visibility = false)) },
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.matchParent
                                height = Dimension.wrapContent
                                this.centerTo(parent)
                            })
                    }

                    if (uiState.banAccountDialogVisible && state.bannedAccountDetails != null) {

                        val period =
                            state.bannedAccountDetails.endDate?.toReadableDateTime(pattern = "yyyy-MM-dd")!!
                        val reason = state.bannedAccountDetails.reason
                        BannedAccountDialog(modifier = Modifier.constrainAs(dialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        }, period = period, reason = reason, onDismissRequest = {
                            event(MappingUiEvent.BannedAccountDialog(visibility = false))
                        })

                    }

                    if (uiState.foregroundLocationPermissionDialogVisible) {
                        DialogForegroundLocationPermission(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismiss = {
                                event(MappingUiEvent.ForegroundLocationPermissionDialog(visibility = false))
                            }
                        )
                    }

                    if (uiState.backgroundLocationPermissionDialogVisible) {
                        DialogBackgroundLocationPermission(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            }, onDismiss = {
                                event(MappingUiEvent.BackgroundLocationPermissionDialog(visibility = false))
                            })
                    }

                    if (uiState.prominentPhoneCallDialogVisible) {

                        AccessPhoneCallDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.DismissProminentPhoneCallDialog)
                            },
                            onDeny = {
                                event(MappingUiEvent.DismissProminentPhoneCallDialog)
                            },
                            onAllow = {
                                event(MappingUiEvent.DismissProminentPhoneCallDialog)
                                event(MappingUiEvent.AllowProminentPhoneCallDialog)
                            })
                    }

                    if (uiState.prominentGalleryDialogVisible) {

                        AccessGalleryDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            }, onDismissRequest = {
                                event(MappingUiEvent.DismissProminentGalleryDialog)
                            }, onDeny = {
                                event(MappingUiEvent.DismissProminentGalleryDialog)
                            }, onAllow = {
                                event(MappingUiEvent.DismissProminentGalleryDialog)
                                event(MappingUiEvent.AllowProminentGalleryDialog)
                            })
                    }



                    if (uiState.prominentCameraDialogVisible) {

                        AccessCameraDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            }, onDismissRequest = {
                                event(MappingUiEvent.DismissProminentCameraDialog)
                            }, onDeny = {
                                event(MappingUiEvent.DismissProminentCameraDialog)
                            }, onAllow = {
                                event(MappingUiEvent.DismissProminentCameraDialog)
                                event(MappingUiEvent.AllowProminentCameraDialog)
                            })
                    }

                    if (uiState.prominentForegroundLocationDialogVisible) {

                        AccessForegroundLocationDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.DismissProminentForegroundLocationDialog)
                            },
                            onDeny = {
                                event(MappingUiEvent.DismissProminentForegroundLocationDialog)
                            },
                            onAllow = {
                                event(MappingUiEvent.DismissProminentForegroundLocationDialog)
                                event(MappingUiEvent.AllowProminentForegroundLocationDialog)
                            })

                    }

                    if (uiState.prominentNotificationDialogVisible) {

                        AccessNotificationDialog(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.DismissProminentNotificationDialog)
                            },
                            onDeny = {
                                event(MappingUiEvent.DismissProminentNotificationDialog)
                            },
                            onAllow = {
                                event(MappingUiEvent.DismissProminentNotificationDialog)
                                event(MappingUiEvent.AllowProminentNotificationDialog)
                            })
                    }

                    if (uiState.filesAndMediaPermissionDialogVisible) {
                        DialogFilesAndMediaPermission(
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismiss = {
                                event(MappingUiEvent.FilesAndMediaPermissionDialog(visibility = false))
                            }
                        )
                    }

                    if (uiState.cameraPermissionDialogVisible) {
                        DialogCameraPermission(
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismiss = {
                                event(MappingUiEvent.CameraPermissionDialog(visibility = false))
                            }
                        )
                    }


                    if (uiState.notificationPermissionVisible) {
                        DialogNotificationPermission(
                            modifier = Modifier.constrainAs(
                                dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismiss = {
                                event(
                                    MappingUiEvent.NotificationPermissionDialog(
                                        visibility = false))
                            }
                        )
                    }


                    if (uiState.accessPhotoDialogVisible) {
                        AccessPhotoDialog(
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.AccessPhotoDialog(visibility = false))
                            },
                            openGallery = {
                                event(MappingUiEvent.SelectImageFromGallery)
                            },
                            takePhoto = {
                                event(MappingUiEvent.OpenCamera)
                            }
                        )
                    }

                    if (uiState.alertDialogState.visible()) {
                        AlertDialog(
                            alertDialog = uiState.alertDialogState,
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = { event(MappingUiEvent.AlertDialog(alertDialogState = AlertDialogState())) })
                    }

                    if (uiState.discardHazardousMarkerDialogVisible) {
                        DiscardHazardousLaneMarkerDialog(
                            modifier = Modifier.constrainAs(dialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            },
                            onDismissRequest = {
                                event(MappingUiEvent.DiscardChangesMarkerDialog(visibility = false))
                            },
                            onClickDiscard = {
                                event(MappingUiEvent.DiscardChangesMarkerDialog(visibility = false))
                                event(MappingUiEvent.DiscardMarkerChanges)
                            }
                        )
                    }



                    if (uiState.requestAcceptedVisible) {

                        MappingRequestAccepted(
                            modifier = Modifier.fillMaxSize(),
                            onClickOkButton = { event(MappingUiEvent.RescueRequestAccepted) },
                            acceptedName = state.rescuee?.name ?: "Name placeholder",
                            onDismiss = {
                                event(MappingUiEvent.RescueRequestAccepted)
                            })
                    }

                    val rescueTransaction = state.rescueTransaction

                    if (uiState.requestCancelledVisible && rescueTransaction != null) {

                        MappingRequestCancelled(
                            modifier = Modifier.fillMaxSize(),
                            onClickOkButton = { event(MappingUiEvent.CancelledRescueConfirmed) },
                            cancelledRescueModel = CancelledRescueModel(
                                transactionID = rescueTransaction.id,
                                rescueCancelledBy = rescueTransaction.getCancellationName(),
                                reason = rescueTransaction.getCancellationReason(),
                                message = rescueTransaction.getCancellationMessage()
                            ), onDismiss = {
                                event(MappingUiEvent.CancelledRescueConfirmed)
                            })
                    }

                }
            }


        }
    }
}
