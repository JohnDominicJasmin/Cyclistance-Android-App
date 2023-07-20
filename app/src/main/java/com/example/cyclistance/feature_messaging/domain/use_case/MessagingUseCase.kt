package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase

data class MessagingUseCase(
    val refreshTokenUseCase: RefreshTokenUseCase
)
