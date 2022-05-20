package com.example.cyclistance.feature_main_screen.domain.use_case

import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.GetCancellationByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.GetRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUserAssistanceByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUsersAssistanceUseCase

data class MappingUseCase(
    val getCancellationByIdUseCase: GetCancellationByIdUseCase,
    val getRescueRequestUseCase: GetRescueRequestUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val getUserAssistanceByIdUseCase: GetUserAssistanceByIdUseCase,
    val getUsersAssistanceUseCase: GetUsersAssistanceUseCase,
)
