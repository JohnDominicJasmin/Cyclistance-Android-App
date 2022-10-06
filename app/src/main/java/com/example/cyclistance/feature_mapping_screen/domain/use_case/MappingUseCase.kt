package com.example.cyclistance.feature_mapping_screen.domain.use_case

import com.example.cyclistance.feature_main_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.UpdateAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.UpdateBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.*

data class MappingUseCase(



    val getUserByIdUseCase: GetUserByIdUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val createUserUseCase: CreateUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val updateUserUseCase: UpdateUserUseCase,

    val getUserLocationUseCase: GetUserLocationUseCase,

    val getBikeTypeUseCase: GetBikeTypeUseCase,
    val updateBikeTypeUseCase: UpdateBikeTypeUseCase,
    val getAddressUseCase: GetAddressUseCase,
    val updateAddressUseCase: UpdateAddressUseCase


)
