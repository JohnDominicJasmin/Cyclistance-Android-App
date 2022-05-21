package com.example.cyclistance.feature_main_screen.domain.use_case

import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.CreateCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.DeleteCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.GetCancellationByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.UpdateCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.CreateRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.DeleteRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.GetRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.UpdateRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.*
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.*

data class MappingUseCase(
    val getCancellationByIdUseCase: GetCancellationByIdUseCase,
    val createCancellationEventUseCase: CreateCancellationEventUseCase,
    val deleteCancellationEventUseCase: DeleteCancellationEventUseCase,
    val updateCancellationEventUseCase: UpdateCancellationEventUseCase,


    val getRescueRequestUseCase: GetRescueRequestUseCase,
    val createRescueRequestUseCase: CreateRescueRequestUseCase,
    val deleteRescueRequestUseCase: DeleteRescueRequestUseCase,
    val updateRescueRequestUseCase: UpdateRescueRequestUseCase,


    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val createUserUseCase: CreateUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val updateUserUseCase: UpdateUserUseCase,


    val getUserAssistanceByIdUseCase: GetUserAssistanceByIdUseCase,
    val getUsersAssistanceUseCase: GetUsersAssistanceUseCase,
    val createUserAssistanceUseCase: CreateUserAssistanceUseCase,
    val deleteUserAssistanceUseCase: DeleteUserAssistanceUseCase,
    val updateUserAssistanceUseCase: UpdateUserAssistanceUseCase,
)
