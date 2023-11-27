package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.report_incident

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AddIncidentImage(
    modifier: Modifier = Modifier,
    addIncidentImage: () -> Unit) {


    val stroke = Stroke(
        width = 5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = modifier

            .drawBehind {
                drawRoundRect(
                    color = Black500,
                    style = stroke,
                    cornerRadius = CornerRadius(12.dp.toPx()))
            }
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                addIncidentImage()
            },
        contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier.padding(vertical = 12.dp)) {

            Icon(
                painter = painterResource(id = R.drawable.ic_add_image),
                contentDescription = "Add Image",
                modifier = Modifier.padding(end = 8.dp))

            Text(
                text = "Add Incident Image",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.button
            )
        }
    }

}

@Preview
@Composable
fun PreviewAddIncidentImage() {
    CyclistanceTheme(darkTheme = true) {

        AddIncidentImage(addIncidentImage = {})
    }
}