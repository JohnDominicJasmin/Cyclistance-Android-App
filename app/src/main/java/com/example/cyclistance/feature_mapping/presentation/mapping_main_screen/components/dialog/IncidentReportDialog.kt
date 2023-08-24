package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DialogAnimatedIconCreator
import com.example.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun IncidentReportDialog(
    marker: HazardousLaneMarker,
    state: MappingState,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onDismissRequest: () -> Unit = {}
) {


    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    DialogAnimatedIconCreator(
        modifier = modifier,
        icon = icon,
        isDialogOpen = isDialogOpen,
        onDialogVisibilityToggle = { onDialogVisibilityToggle(!isDialogOpen) },
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            Box(contentAlignment = Alignment.Center, modifier = Modifier) {
                Text(
                    text = marker.label,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(vertical = 7.dp)
                        .align(Alignment.Center)
                )

                if(state.userId == marker.idCreator) {
                    DropDownMenu(
                        iconImageVector = Icons.Default.MoreHoriz,
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center),
                        onClickEdit = { },
                        onClickDelete = { }
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                ReportItemsSection(
                    marker = marker
                )

                Button(
                    shape = RoundedCornerShape(12.dp),
                    onClick = {},
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
}


@Composable
fun ReportItemsSection(modifier: Modifier = Modifier, marker: HazardousLaneMarker) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)) {

        ReportItemDescription(
            modifier = Modifier.padding(vertical = 4.dp),
            iconImage = Icons.Default.AccessTimeFilled,
            iconDescription = "Date and Time",
            description = marker.datePosted.toReadableDateTime()
        )

        ReportItemDescription(
            modifier = Modifier.padding(vertical = 4.dp),
            iconImage = Icons.Default.LocationOn,
            iconDescription = "Address",
            description = marker.address
        )

        ReportItemDescription(
            modifier = Modifier.padding(vertical = 4.dp),
            iconImage = Icons.Default.Info,
            iconDescription = "Description",
            description = marker.description
        )

    }
}

@Composable
private fun ReportItemDescription(
    modifier: Modifier = Modifier,
    iconImage: ImageVector,
    iconDescription: String,
    description: String = "") {

    Column(
        modifier = modifier.wrapContentWidth(),
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


@Preview
@Composable
fun PreviewReportItemDescription() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ReportItemDescription(
                modifier = Modifier,
                iconImage = Icons.Default.AccessTimeFilled,
                iconDescription = "Date and Time",
                description = "12/12/2021 12:00")
        }
    }
}

@Preview
@Composable
fun PreviewIncidentReportDialog() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            IncidentReportDialog(
                icon = R.drawable.ic_lane_closure_marker,
                onDismissRequest = {},
                marker = HazardousLaneMarker(
                    id = "1",
                    label = "Crash",
                    latitude = MappingConstants.DEFAULT_LATITUDE,
                    longitude = MappingConstants.DEFAULT_LONGITUDE,
                    idCreator = "1o3jjt90qin3f9n23",
                    datePosted = Date(1692720000000),
                    description = "Help me, I crashed my bike and I need help please help me I'm dying",
                    address = "Calle 1 # 2-3"
                ), state = MappingState(userId = "1o3jjt90qin3f9n23"))
        }
    }
}