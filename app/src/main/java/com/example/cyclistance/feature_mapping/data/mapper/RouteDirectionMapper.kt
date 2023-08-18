package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.mapbox.api.directions.v5.models.DirectionsRoute

object RouteDirectionMapper {
    fun DirectionsRoute.toRouteDirection() = RouteDirection(
        geometry = this.geometry() ?: "",
    )
}