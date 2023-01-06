package com.example.cyclistance.feature_mapping.domain.use_case.bike_type

import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class SetBikeTypeUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(bikeType: String?){

        if(bikeType!!.isEmpty()){
            throw MappingExceptions.BikeTypeException()
        }
        repository.setBikeType(bikeType)
    }
}