package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class GetNameUseCase(private val repository: SettingRepository) {

     suspend operator fun invoke(): String {

        return repository.getName().takeIf { !it.isNullOrEmpty() } ?: throw MappingExceptions.NameException(message = "Name not found")

    }
}