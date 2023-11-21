package com.myapp.cyclistance.feature_mapping.domain.use_case.routes

import com.mapbox.geojson.Point
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetRouteDirectionsUseCase(private val mappingRepository: MappingRepository) {
    suspend operator fun invoke(origin: Point, destination: Point): RouteDirection {
        return mappingRepository.getRouteDirections(origin, destination)
    }

}