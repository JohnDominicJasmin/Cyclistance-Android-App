package com.example.cyclistance.feature_mapping.domain.helper

import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class NearbyCyclistHandler(

    val state: MutableStateFlow<MappingState>,
    val eventFlow: MutableSharedFlow<MappingEvent>) {


    fun updateClient(){

        val rescueTransaction = state.value.rescueTransaction
        val userRole = state.value.user.getRole()

        if(userRole == Role.RESCUEE.name.lowercase()){
            rescueTransaction?.rescuerId?.let {

            }
        }
    }


}