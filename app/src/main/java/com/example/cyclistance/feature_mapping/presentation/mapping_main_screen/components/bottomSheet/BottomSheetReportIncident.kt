package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Orange800
import com.example.cyclistance.theme.Red900
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetReportIncident(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClick: (label: String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val isDarkTheme = IsDarkTheme.current


    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                elevation = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Divider(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(0.1f)
                        .padding(top = 4.dp),
                    color = MaterialTheme.colors.primary,
                    thickness = 2.dp)


                Icon(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_close_darktheme else R.drawable.ic_close_lighttheme),
                    contentDescription = "Close",
                    modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.CenterEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .clickable {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                    tint = Color.Unspecified
                )

            }

            Text(
                text = "Report an Incident",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 16.dp)
            )



            Column(
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {

                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_construction,
                        incidentLabel = "Construction",
                        onClick = onClick
                    )

                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_lane_closure,
                        incidentLabel = "Lane closure",
                        onClick = onClick
                    )

                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_car_crash,
                        incidentLabel = "Crash",
                        buttonColor = Red900,
                        onClick = onClick
                    )


                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {


                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_need_assistance,
                        incidentLabel = "Need Assistance",
                        onClick = onClick
                    )
                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_object_on_road,
                        incidentLabel = "Object on Road",
                        onClick = onClick
                    )
                    IncidentItem(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.3f),
                        icon = R.drawable.ic_slowdown,
                        incidentLabel = "Slowdown",
                        buttonColor = Red900,
                        onClick = onClick
                    )

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun IncidentItem(
    modifier: Modifier,
    @DrawableRes icon: Int,
    incidentLabel: String,
    buttonColor: Color = Orange800,
    onClick: (label: String) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            shape = CircleShape,
            modifier = Modifier.size(54.dp),
            color = buttonColor,
            onClick = { onClick(incidentLabel) }) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "$incidentLabel Icon",
                modifier = Modifier.padding(all = 12.dp)
            )
        }

        Text(
            text = incidentLabel,
            color = Black440,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.SemiBold),
            overflow = TextOverflow.Clip, textAlign = TextAlign.Center,
        )

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomSheetReportIncidentDark() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {
            BottomSheetReportIncident(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                onClick = {

                })
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomSheetReportIncidentLight() {
    val isDarkTheme = false
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {
            BottomSheetReportIncident(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                onClick = {

                })
        }
    }
}


