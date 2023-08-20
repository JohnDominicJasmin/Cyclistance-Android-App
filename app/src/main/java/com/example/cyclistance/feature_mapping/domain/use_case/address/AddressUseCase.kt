package com.example.cyclistance.feature_mapping.domain.use_case.address

import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.flow.Flow

class AddressUseCase(private val repository: MappingUiStoreRepository) {
     suspend operator fun invoke(): Flow<String> {
       return repository.getAddress()
    }
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