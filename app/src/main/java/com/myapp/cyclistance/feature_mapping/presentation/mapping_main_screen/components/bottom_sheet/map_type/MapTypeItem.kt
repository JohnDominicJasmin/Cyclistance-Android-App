package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.map_type

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black500

@Composable
fun MapTypeItem(
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