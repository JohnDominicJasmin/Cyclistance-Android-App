package com.example.cyclistance.feature_mapping_screen.domain.use_case.address

import com.example.cyclistance.core.utils.InputValidate
import com.example.cyclistance.core.utils.InputValidate.numberOfCharactersEnough
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class UpdateAddressUseCase(private val repository: MappingRepository) {

    suspend operator fun invoke(address: String){
        if(address.isEmpty()){
            throw MappingExceptions.AddressException()
        }
        if(!address.numberOfCharactersEnough()){
            throw MappingExceptions.AddressException("Address is too short")
        }

        repository.updateAddress(address)
    }
}