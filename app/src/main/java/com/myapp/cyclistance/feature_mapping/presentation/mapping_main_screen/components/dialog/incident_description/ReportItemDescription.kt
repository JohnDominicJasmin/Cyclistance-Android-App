package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black500

@Composable
fun ReportItemDescription(
    modifier: Modifier = Modifier,
    iconImage: ImageVector,
    iconDescription: String,
    description: String = "",
    isTextClickable: Boolean = false,
    textColor: Color? = Color.Unspecified,
    onClickText: () -> Unit = {}) {

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

                if(isTextClickable){
                    ClickableText(
                        modifier = modifier,
                        text = buildAnnotatedString {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.button.copy(
                                    color = textColor ?: Color.Unspecified,
                                    fontWeight = FontWeight.Medium)
                            )
                        }, onClick = { onClickText() })
                }else{
                    Text(
                        text = description,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier.padding(vertical = 4.dp))

                }

            }
        }

    }
}
