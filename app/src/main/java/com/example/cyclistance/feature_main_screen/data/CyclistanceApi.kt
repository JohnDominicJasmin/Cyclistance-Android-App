package com.example.cyclistance.feature_main_screen.data

import com.example.cyclistance.feature_main_screen.data.remote.dto.*
import retrofit2.http.*

interface CyclistanceApi {


    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserDto

    @GET("/api/v1/get-users")
    suspend fun getUsers(): List<UserDto>

    @POST("/api/v1/create-user")
    suspend fun createUser(@Body userDto: UserDto)

    @PATCH("/api/v1/update-user/{item_id}")
    suspend fun updateUser(@Path("item_id") itemId: String, @Body userDto: UserDto)

    @DELETE("/api/v1/delete-user/{item_id}")
    suspend fun deleteUser(@Path("item_id") itemId: String)


    @GET("/api/v1/get-rescue-request/{event_id}")
    suspend fun getRescueRequest(@Path("event_id") eventId: String): RescueRequestDto

    @POST("/api/v1/create-rescue-request")
    suspend fun createRescueRequest(@Body rescueRequestDto: RescueRequestDto)

    @PATCH("/api/v1/update-rescue-request/{event_id}")
    suspend fun updateRescueRequest(
        @Path("event_id") eventId: String,
        @Body rescueRequestDto: RescueRequestDto)

    @DELETE("/api/v1/delete-rescue-request/{event_id}")
    suspend fun deleteRescueRequest(@Path("event_id") eventId: String)


    @GET("/api/v1/get-cancellation-event/{id},{client_id}")
    suspend fun getCancellationById(
        @Path("id") id: String,
        @Path("client_id") clientId: String
    ): CancellationEventDto

    @POST("/api/v1/create-cancellation-event")
    suspend fun createCancellationEvent(@Body cancellationEventDto: CancellationEventDto)

    @PATCH("/api/v1/update-cancellation-event/{item_id}")
    suspend fun updateCancellationEvent(
        @Path("item_id") itemId: String,
        @Body cancellationEventDto: CancellationEventDto)

    @DELETE("/api/v1/delete-cancellation-event/{item_id}")
    suspend fun deleteCancellationEvent(@Path("item_id") itemId: String)

}






















