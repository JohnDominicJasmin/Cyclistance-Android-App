package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.first

class GetPhoneNumberUseCase(private val repository: SettingRepository) {
    suspend operator fun invoke(): String {
        return repository.getPhoneNumber().first().takeIf { !it.isNullOrEmpty() }
               ?: throw SettingExceptions.PhoneNumberException()
    }
}