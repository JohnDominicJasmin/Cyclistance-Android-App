package com.example.cyclistance.feature_mapping_screen.domain.use_case.routes

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import com.mapbox.geojson.Point

class GetRouteDirectionsUseCase(private val mappingRepository: MappingRepository) {
    suspend operator fun invoke(origin: Point, destination: Point) =
        mappingRepository.getRouteDirections(origin, destination)
}