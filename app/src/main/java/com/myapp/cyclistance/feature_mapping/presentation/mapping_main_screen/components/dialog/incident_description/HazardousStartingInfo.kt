package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme

@Composable
fun HazardousStartingInfo(

    modifier: Modifier = Modifier,
    onClickGotItButton: () -> Unit) {

    val isDarkTheme = IsDarkTheme.current


    Surface(modifier = modifier) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 8.dp)) {

            Image(
                painter = painterResource(id = if (isDarkTheme) R.drawable.ic_marker_info_dark else R.drawable.ic_marker_info_light),
                contentDescription = "Image",
                modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "Hazardous Marker Visibility",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))

            Text(
                text = "Your Hazardous Lane Marker\n" +
                       "will only be visible for a week",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(vertical = 4.dp))

            Button(
                onClick = onClickGotItButton,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary),
                modifier = Modifier.padding(vertical = 12.dp)) {

                Text(
                    text = "Got it!",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp))
            }

        }


    }
}
