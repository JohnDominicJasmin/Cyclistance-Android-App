package com.example.cyclistance.feature_main_screen.domain.use_case.bike_type

import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository

class UpdateBikeTypeUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(value: String?){

        if(value!!.isEmpty()){
            throw MappingExceptions.BikeTypeException()
        }
        repository.updateBikeType(value)
    }
}