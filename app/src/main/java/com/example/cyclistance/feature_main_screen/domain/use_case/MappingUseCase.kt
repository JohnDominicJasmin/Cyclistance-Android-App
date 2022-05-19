package com.example.cyclistance.feature_main_screen.domain.use_case

import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.GetCancellationByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.help_request.GetHelpRequestByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUserAssistanceByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUsersAssistanceUseCase

data class MappingUseCase(
    val getCancellationByIdUseCase: GetCancellationByIdUseCase,
    val getHelpRequestByIdUseCase: GetHelpRequestByIdUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val getUserAssistanceByIdUseCase: GetUserAssistanceByIdUseCase,
    val getUsersAssistanceUseCase: GetUsersAssistanceUseCase,
)
