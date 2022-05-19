package com.example.cyclistance.feature_main_screen.data.mapper

import com.example.cyclistance.feature_main_screen.data.remote.dto.*
import com.example.cyclistance.feature_main_screen.domain.model.*

object UserMapper {

fun UserDto.toUser():User{
    return User(
        address = this.address,
        id = this.id,
        location = this.location,
        name = this.name
    )
}


fun UserAssistanceDto.toUserAssistance():UserAssistance{
    return UserAssistance(
        confirmationDetails = this.confirmationDetails,
        id = this.id,
        rescueRequest = this.rescueRequest,
        status = this.status
    )
}



fun HelpRequestDto.toHelpRequest():HelpRequest{
    return HelpRequest(
        accepted = this.accepted,
        clientId = this.clientId,
        id = this.id
    )
}


fun CancellationEventDto.toCancellationEvent():CancellationEvent{
    return CancellationEvent(
        cancellationReason = this.cancellationReason,
        clientId = this.clientId,
        id = this.id
    )
}




}