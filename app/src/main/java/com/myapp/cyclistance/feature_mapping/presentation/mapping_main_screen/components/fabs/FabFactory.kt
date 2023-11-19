package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.navigation.IsDarkTheme

@Composable
fun FabFactory(
    modifier: Modifier = Modifier,
    iconId: Int = -1,
    onClick: () -> Unit = {},
    contentDescription: String = "",
    tint: Color = MaterialTheme.colors.onSurface

) {

    val isDarkTheme = IsDarkTheme.current

   Button( modifier = modifier.shadow(elevation = if(isDarkTheme) 0.dp else 4.dp, shape = CircleShape),
       shape = CircleShape,
       contentPadding = PaddingValues(0.dp),
       colors = ButtonDefaults.buttonColors(
           backgroundColor = MaterialTheme.colors.surface,
       ), onClick = onClick) {

       Icon(
           painter = painterResource(id = iconId),
           contentDescription = contentDescription,
           tint = tint
       )
   }
}

@Preview(name = "FabFactory")
@Composable
private fun PreviewFabFactory() {
    FabFactory()
}