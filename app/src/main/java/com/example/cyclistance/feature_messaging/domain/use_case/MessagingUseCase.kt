package com.example.cyclistance.feature_messaging.domain.use_case

import com.example.cyclistance.feature_messaging.domain.use_case.manage_user.GetUsersUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.token.RefreshTokenUseCase

data class MessagingUseCase(
    val refreshTokenUseCase: RefreshTokenUseCase,
    val getUsersUseCase: GetUsersUseCase
)
