package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MapType
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapTypeBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: MappingState,
    onToggleDefaultMapType: () -> Unit,
    onToggleTrafficMapType: () -> Unit,
    onToggleHazardousMapType: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isDarkTheme = IsDarkTheme.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {


        Box(modifier = Modifier.fillMaxWidth()) {
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

            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Divider(
                    modifier = Modifier

                        .fillMaxWidth(0.08f)
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colors.primary,
                    thickness = 1.7.dp)



                Text(
                    text = "Select Map Types",
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {

                    MapTypeItem(
                        modifier = Modifier.weight(1f),
                        isSelected = state.hazardousMapTypeSelected,
                        imageId = if (isDarkTheme) R.drawable.ic_map_type_hazardous_dark else R.drawable.ic_map_type_hazardous_light,
                        mapTypeDescription = MapType.HazardousLane.type,
                        onClick = onToggleHazardousMapType)


                    MapTypeItem(
                        modifier = Modifier.weight(1f),
                        isSelected = state.trafficMapTypeSelected,
                        imageId = if (isDarkTheme) R.drawable.ic_traffic_dark else R.drawable.ic_traffic_light,
                        mapTypeDescription = MapType.Traffic.type,
                        onClick = onToggleTrafficMapType)

                    MapTypeItem(
                        modifier = Modifier.weight(1f),
                        isSelected = state.defaultMapTypeSelected,
                        imageId = if (isDarkTheme) R.drawable.ic_nearby_cyclist_dark else R.drawable.ic_nearby_cyclist_light,
                        mapTypeDescription = MapType.NearbyCyclists.type,
                        onClick = onToggleDefaultMapType)

                }
            }

        }
    }
}




@Composable
private fun MapTypeItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    @DrawableRes imageId: Int,
    mapTypeDescription: String,
    onClick: () -> Unit) {


    Column(
        modifier = modifier.padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {


        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Map Type Image",
            modifier = Modifier
                .size(88.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .border(
                    2.dp,
                    if (isSelected) MaterialTheme.colors.primary else Color.Transparent,
                    shape = RoundedCornerShape(16.dp))
                .clickable { onClick() })



        Text(
            text = mapTypeDescription,
            color = if (isSelected) MaterialTheme.colors.primary else Black500,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun PreviewMapTypeItemDark() {

    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            MapTypeItem(
                modifier = Modifier,
                imageId = R.drawable.ic_map_type_hazardous_dark,
                mapTypeDescription = "Hazardous Lane",
                isSelected = true,
                onClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomSheetHazardousLane() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {
                MapTypeBottomSheet(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    state = MappingState(),
                    onToggleDefaultMapType = {

                    },
                    onToggleHazardousMapType = {

                    },
                    onToggleTrafficMapType = {

                    }
                )
            }
        }
    }
}