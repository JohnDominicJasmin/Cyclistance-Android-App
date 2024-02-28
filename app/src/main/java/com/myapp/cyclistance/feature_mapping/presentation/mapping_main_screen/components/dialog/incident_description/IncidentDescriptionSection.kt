package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun IncidentDescriptionSection(
    onDismissBottomSheet: () -> Unit,
    @DrawableRes icon: Int,
    uiState: MappingUiState,
    state: MappingState,
    marker: HazardousLaneMarkerDetails,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    viewProofIncident: () -> Unit) {


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
                modifier = Modifier.fillMaxWidth(),
                viewProofIncident = viewProofIncident
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


@Preview
@Composable
fun PreviewIncidentDescriptionSection() {
    CyclistanceTheme(darkTheme = true){
        IncidentDescriptionSection(
            onDismissBottomSheet = {},
            icon = R.drawable.ic_construction_marker,
            uiState = MappingUiState(),
            state = MappingState(),
            marker = HazardousLaneMarkerDetails(
                id = "1",
                idCreator = "1",
                label = "Construction",
                description = "Construction",
                latitude = 0.0,
                longitude = 0.0,
               datePosted = Date(),
                address = "Tanauan Batangas",
                incidentImageUri = ""

            ),
            onClickEdit = {},
            onClickDelete = {},
            viewProofIncident = {}
        )
    }
    
}