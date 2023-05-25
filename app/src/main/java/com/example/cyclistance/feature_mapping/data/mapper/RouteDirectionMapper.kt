package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteDirection
import com.mapbox.api.directions.v5.models.DirectionsRoute

object RouteDirectionMapper {
    fun DirectionsRoute.toRouteDirection() = RouteDirection(
        geometry = this.geometry() ?: "",
        duration = this.duration()
    )
}