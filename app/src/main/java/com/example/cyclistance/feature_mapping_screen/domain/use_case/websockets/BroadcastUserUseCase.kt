package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastUserUseCase(private val repository: MappingRepository) {
    operator fun invoke() {
        repository.broadCastUser()
    }
}