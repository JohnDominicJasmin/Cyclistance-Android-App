package com.example.cyclistance.feature_mapping.domain.use_case.address

import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class SetAddressUseCase(private val repository: MappingRepository) {

    suspend operator fun invoke(address: String){
        if(address.isEmpty()){
            throw MappingExceptions.AddressException()
        }
        if(!address.numberOfCharactersEnough()){
            throw MappingExceptions.AddressException("Address is too short")
        }

        repository.setAddress(address)
    }
}