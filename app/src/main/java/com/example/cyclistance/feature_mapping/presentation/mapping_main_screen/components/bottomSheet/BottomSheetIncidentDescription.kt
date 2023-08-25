package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.example.cyclistance.core.utils.validation.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetIncidentDescription(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    uiState: MappingUiState,
    state: MappingState,
    @DrawableRes icon: Int,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    onClickOkay: () -> Unit
) {
    val scope = rememberCoroutineScope()
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

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {


                IconButton(
                    onClick = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }, modifier = Modifier.align(Alignment.TopEnd)
                ) {

                    Icon(
                        painter = painterResource(id = if (isDarkTheme) R.drawable.ic_close_darktheme else R.drawable.ic_close_lighttheme),
                        contentDescription = "Close",
                        tint = Color.Unspecified
                    )

                }


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
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.08f)
                            .padding(top = 4.dp),
                        color = MaterialTheme.colors.primary,
                        thickness = 2.dp)

                    Icon(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .shadow(if (isDarkTheme) 0.dp else 8.dp, shape = CircleShape),
                        painter = painterResource(id = icon),
                        contentDescription = "Marker Icon Description",
                        tint = Color.Unspecified)

                    Text(
                        text = uiState.selectedHazardousMarker.label,
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
                    onClick = onClickOkay,
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
            description = marker.datePosted.toReadableDateTime()
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

            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberBottomSheetState(
                    initialValue = BottomSheetValue.Expanded))
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                BottomSheetIncidentDescription(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    uiState = MappingUiState(
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
                    onClickOkay = {})
            }
        }
    }
}