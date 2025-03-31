package com.myapp.cyclistance.feature_mapping.data.mapper

import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection

object RouteDirectionMapper {
    fun DirectionsRoute.toRouteDirection() = RouteDirection(
        geometry = this.geometry() ?: "",
    )
}