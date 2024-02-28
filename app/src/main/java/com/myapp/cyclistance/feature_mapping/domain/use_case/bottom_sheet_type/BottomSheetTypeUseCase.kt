package com.myapp.cyclistance.feature_mapping.domain.use_case.bottom_sheet_type

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository

class BottomSheetTypeUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke() = repository.getBottomSheetType()
    suspend operator fun invoke(bottomSheet: String) {
        repository.setBottomSheetType(bottomSheet)
    }
}