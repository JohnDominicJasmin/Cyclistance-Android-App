package com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository

class ShouldHazardousStartingInfoUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke(shouldShow: Boolean) = repository.showHazardousStartingInfo(shouldShow)
    suspend operator fun invoke() = repository.shouldShowHazardousStartingInfo()
}