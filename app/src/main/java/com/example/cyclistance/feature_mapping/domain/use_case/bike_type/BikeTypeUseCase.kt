package com.example.cyclistance.feature_mapping.domain.use_case.bike_type

import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.flow.Flow


class BikeTypeUseCase(private val repository: MappingUiStoreRepository) {

    suspend operator fun invoke(): Flow<String> {
        return repository.getBikeType()
    }
    suspend operator fun invoke(bikeType: String?){

        if(bikeType!!.isEmpty()){
            throw MappingExceptions.BikeTypeException()
        }
        repository.setBikeType(bikeType)
    }

}