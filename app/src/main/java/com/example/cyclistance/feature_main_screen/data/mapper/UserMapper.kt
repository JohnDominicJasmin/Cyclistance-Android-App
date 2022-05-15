package com.example.cyclistance.feature_main_screen.data.mapper

import com.example.cyclistance.feature_main_screen.data.remote.dto.*
import com.example.cyclistance.feature_main_screen.domain.model.*

object UserMapper {

    fun CancellationDto.toCancellation(): Cancellation {
        return Cancellation(
            id = this.id,
            clientId = this.clientId,
            cancellationReasons = this.cancellationReasons
        )
    }

    fun HelpRequestDto.toHelpRequest(): HelpRequest {
        return HelpRequest(
            id = this.id,
            clientId = this.clientId,
            accepted = this.accepted
        )
    }

    fun RescueRequestDto.toRescueRequest(): RescueRequest {
        return RescueRequest(
            id = this.id,
            respondents = this.respondents,
        )
    }

    fun UserAssistanceDto.toUserAssistance(): UserAssistance {
        return UserAssistance(
            id = this.id,
            confirmationDetail = this.confirmationDetail,
            status = this.status
        )
    }

    fun UserDto.toUser(): User {
        return User(
            id = this.id,
            name = this.name,
            address = this.address,
            location = this.location
        )
    }


}