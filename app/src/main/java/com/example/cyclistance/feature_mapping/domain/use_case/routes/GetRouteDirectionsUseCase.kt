package com.example.cyclistance.feature_mapping.domain.use_case.routes

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.RouteDirection
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.mapbox.geojson.Point

class GetRouteDirectionsUseCase(private val mappingRepository: MappingRepository, private val context: Context) {
    suspend operator fun invoke(origin: Point, destination: Point): RouteDirection {

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        return mappingRepository.getRouteDirections(origin, destination)
    }

}