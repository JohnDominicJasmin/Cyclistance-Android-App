package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black500

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HazardousIncidentIndicator(modifier: Modifier = Modifier, pagerState: PagerState) {

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(6) { iteration ->
            val isSelect = pagerState.currentPage == iteration
            val color = if (isSelect) MaterialTheme.colors.primary else Black500
            Box(
                modifier = Modifier
                    .padding(1.5.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(9.dp)

            )
        }
    }
}