package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.example.cyclistance.core.utils.constants.MappingConstants.CONSTRUCTION
import com.example.cyclistance.core.utils.constants.MappingConstants.CRASH
import com.example.cyclistance.core.utils.constants.MappingConstants.LANE_CLOSURE
import com.example.cyclistance.core.utils.constants.MappingConstants.NEED_ASSISTANCE
import com.example.cyclistance.core.utils.constants.MappingConstants.OBJECT_ON_ROAD
import com.example.cyclistance.core.utils.constants.MappingConstants.SLOWDOWN
import com.example.cyclistance.core.utils.formatter.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun BottomSheetIncidentDescription(
    modifier: Modifier = Modifier,
    uiState: MappingUiState,
    state: MappingState,
    @DrawableRes icon: Int,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    onClickCancelButton: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onClickConfirmButton: (description: String, label: String) -> Unit

    ) {


    val isDarkTheme = IsDarkTheme.current
    val marker = uiState.selectedHazardousMarker!!
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                elevation = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()) {


            IconButton(
                onClick = onDismissBottomSheet, modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(100f)
            ) {

                Icon(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_close_darktheme else R.drawable.ic_close_lighttheme),
                    contentDescription = "Close",
                    tint = Color.Unspecified
                )

            }


            if (uiState.currentlyEditingHazardousMarker != null) {

                val marker = uiState.currentlyEditingHazardousMarker
                IncidentDescriptionEditMode(
                    modifier = Modifier,
                    markerLabel = marker.label,
                    markerDescription = marker.description,
                    onClickCancelButton = onClickCancelButton,
                    onClickConfirmButton = onClickConfirmButton
                )
            } else {
                IncidentDescriptionSection(
                    onDismissBottomSheet = onDismissBottomSheet,
                    icon = icon,
                    uiState = uiState,
                    state = state,
                    marker = marker,
                    onClickEdit = onClickEdit,
                    onClickDelete = onClickDelete
                )

            }
        }
    }
}

@Composable
private fun IncidentDescriptionSection(
    onDismissBottomSheet: () -> Unit,
    @DrawableRes icon: Int,
    uiState: MappingUiState,
    state: MappingState,
    marker: HazardousLaneMarker,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit) {


    val isDarkTheme = IsDarkTheme.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                ) {


            if (state.userId == marker.idCreator) {
                DropDownMenu(
                    iconImageVector = Icons.Default.MoreHoriz,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomEnd),
                    onClickEdit = onClickEdit,
                    onClickDelete = onClickDelete
                )
            }



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
            ) {

                Icon(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .shadow(
                            if (isDarkTheme) 0.dp else 8.dp,
                            shape = CircleShape),
                    painter = painterResource(id = icon),
                    contentDescription = "Marker Icon Description",
                    tint = Color.Unspecified)

                Text(
                    text = uiState.selectedHazardousMarker!!.label,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                )

            }

        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)) {

            ReportItemsSection(
                marker = marker,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                shape = RoundedCornerShape(12.dp),
                onClick = onDismissBottomSheet,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(
                    text = "Okay",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    style = MaterialTheme.typography.button)
            }
        }

    }
}
private val incidentMarkers = listOf(
    CONSTRUCTION to R.drawable.ic_construction_marker,
    LANE_CLOSURE to R.drawable.ic_lane_closure_marker,
    CRASH to R.drawable.ic_crash_marker,
    NEED_ASSISTANCE to R.drawable.ic_need_assistance_marker,
    OBJECT_ON_ROAD to R.drawable.ic_object_on_road_marker,
    SLOWDOWN to R.drawable.ic_slow_down_marker
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IncidentDescriptionEditMode(
    modifier: Modifier = Modifier,
    markerLabel: String,
    markerDescription: String,
    onClickCancelButton: () -> Unit,
    onClickConfirmButton: (description: String, label: String) -> Unit) {

    val pagerState = rememberPagerState(pageCount = {
        6
    })

    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val descriptionChanges = remember( description) {
        description.text != markerDescription
    }
    val labelChanges = remember(pagerState.currentPage) {
        incidentMarkers[pagerState.currentPage].first != markerLabel
    }

    val inputChanges = remember(descriptionChanges, labelChanges) {
        descriptionChanges || labelChanges
    }


    LaunchedEffect(key1 = markerDescription){
        description = TextFieldValue(markerDescription)
    }


    LaunchedEffect(key1 = true) {
        pagerState.scrollToPage(page = incidentMarkers.indexOfFirst { it.first == markerLabel })
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Edit Hazardous Marker",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
        )

        HorizontalPager(
            state = pagerState, pageSize = PageSize.Fill,
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically) { pageIndex ->

            val item = incidentMarkers[pageIndex]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterHorizontally)) {

                Image(
                    modifier = Modifier.size(55.dp),
                    painter = painterResource(id = item.second),
                    contentDescription = "${item.first} icon",
                )

                Text(
                    text = item.first,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal))

            }
        }


        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(6) { iteration ->
                val isSelect = pagerState.currentPage == iteration
                val color = if (isSelect) MaterialTheme.colors.primary else Black500
                Box(
                    modifier = Modifier
                        .padding(1.5.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(9.dp)

                )
            }
        }

        AdditionalMessage(
            text = "Description",
            placeholderText = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(120.dp),
            message = description,
            enabled = true,
            onChangeValueMessage = {
                description = it
            })

        ButtonNavigation(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp),
            positiveButtonEnabled = inputChanges,
            onClickNegativeButton = onClickCancelButton,
            onClickPositiveButton = {
                onClickConfirmButton(
                    description.text,
                    incidentMarkers[pagerState.currentPage].first)
            },
        )


    }

}


@Composable
private fun ReportItemsSection(modifier: Modifier = Modifier, marker: HazardousLaneMarker) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)) {

        ReportItemDescription(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            iconImage = Icons.Default.AccessTimeFilled,
            iconDescription = "Date and Time",
            description = marker.datePosted.toReadableDateTime(pattern = "MMM dd, yyyy hh:mm a")
        )

        ReportItemDescription(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            iconImage = Icons.Default.LocationOn,
            iconDescription = "Address",
            description = marker.address
        )


        marker.description.takeIf { it.isNotEmpty() }?.let { description ->
            ReportItemDescription(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                iconImage = Icons.Default.Info,
                iconDescription = "Description",
                description = description
            )
        }

    }
}

@Composable
private fun ReportItemDescription(
    modifier: Modifier = Modifier,
    iconImage: ImageVector,
    iconDescription: String,
    description: String = "") {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start) {


        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = iconImage,
                contentDescription = iconDescription,
                tint = Black500,
                modifier = Modifier.padding(end = 5.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = iconDescription,
                    color = Black500,
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(top = 5.dp))

                Text(
                    text = description,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.padding(vertical = 4.dp))

            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewBottomSheetIncidentDescription() {

    val isDarkTheme = true
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {


            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                BottomSheetIncidentDescription(

                    uiState = MappingUiState(
                        currentlyEditingHazardousMarker = HazardousLaneMarker(label = "Crash", description = "Lorem ipsum dolor sit amet consectetur adipi"),
                        selectedHazardousMarker = HazardousLaneMarker(
                            id = "1",
                            label = "Crash",
                            latitude = 14.123,
                            longitude = 121.123,
                            idCreator = "1o3jjt90qin3f9n23",
                            description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            address = "Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                        )),
                    icon = R.drawable.ic_lane_closure_marker,
                    state = MappingState(userId = "1o3jjt90qin3f39n23"),
                    onClickDelete = {},
                    onClickEdit = {},
                    onDismissBottomSheet = {},
                    onClickCancelButton = {},
                    onClickConfirmButton = { _, _ ->})
            }
        }
    }
}

@Preview
@Composable
fun PreviewIncidentDescriptionEditMode() {
    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.BottomCenter) {
            IncidentDescriptionEditMode(
                onClickCancelButton = { /*TODO*/ },
                onClickConfirmButton = { description, label ->  },
                markerLabel = "Crash", modifier = Modifier, markerDescription = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Lorem ipsum dolor sit amet consectetur adipisicing elit.")
        }
    }
}
