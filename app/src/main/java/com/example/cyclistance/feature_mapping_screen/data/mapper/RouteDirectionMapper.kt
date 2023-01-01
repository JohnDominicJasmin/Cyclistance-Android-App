package com.example.cyclistance.feature_mapping_screen.data.mapper

import com.example.cyclistance.feature_mapping_screen.domain.model.RouteDirection
import com.mapbox.api.directions.v5.models.DirectionsRoute

object RouteDirectionMapper {
    fun DirectionsRoute.toRouteDirection() = RouteDirection(
        geometry = this.geometry() ?: "",
        duration = this.duration() ?: 0.00
    )
}