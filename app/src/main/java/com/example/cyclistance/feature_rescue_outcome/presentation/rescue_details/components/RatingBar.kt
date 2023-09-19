package com.example.cyclistance.feature_rescue_outcome.presentation.rescue_details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.utils.constants.MappingConstants.NUMBER_OF_STARS
import com.example.cyclistance.theme.CyclistanceTheme
import java.lang.Math.ceil
import java.lang.Math.floor


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    starsColor: Color = MaterialTheme.colors.primary,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (NUMBER_OF_STARS - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Outlined.StarHalf,
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

@Preview
@Composable
fun RatingPreview() {
    CyclistanceTheme(darkTheme = true) {
        RatingBar(rating = 2.5)
    }
}

@Preview
@Composable
fun RatingPreviewFull() {
    CyclistanceTheme(darkTheme = true) {
        RatingBar(rating = 5.0)
    }
}

@Preview
@Composable
fun RatingPreviewWorst() {
    CyclistanceTheme(darkTheme = true) {
        RatingBar(rating = 1.0)
    }
}

@Preview
@Composable
fun RatingPreviewDisabled() {
    CyclistanceTheme(darkTheme = true) {
        RatingBar(rating = 0.0, starsColor = Color.Gray)
    }
}