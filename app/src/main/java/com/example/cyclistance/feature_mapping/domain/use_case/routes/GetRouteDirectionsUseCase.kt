package com.example.cyclistance.feature_mapping.domain.use_case.routes

import com.example.cyclistance.feature_mapping.domain.model.RouteDirection
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.mapbox.geojson.Point

class GetRouteDirectionsUseCase(private val mappingRepository: MappingRepository) {
    suspend operator fun invoke(origin: Point, destination: Point): RouteDirection {
        return mappingRepository.getRouteDirections(origin, destination)
    }

}