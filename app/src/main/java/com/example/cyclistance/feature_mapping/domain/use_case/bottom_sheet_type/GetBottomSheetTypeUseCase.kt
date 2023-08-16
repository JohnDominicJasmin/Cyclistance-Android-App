package com.example.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type

import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository

class GetBottomSheetTypeUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke() = repository.getBottomSheetType()
}