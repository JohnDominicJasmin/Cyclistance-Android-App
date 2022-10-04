package com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type

import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class UpdateBikeTypeUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(bikeType: String?){

        if(bikeType!!.isEmpty()){
            throw MappingExceptions.BikeTypeException()
        }
        repository.updateBikeType(bikeType)
    }
}