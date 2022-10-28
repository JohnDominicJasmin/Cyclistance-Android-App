package com.example.cyclistance.feature_mapping_screen.data

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.*
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserDto
import retrofit2.http.*

interface CyclistanceApi {


    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserDto

    @GET("/api/v1/get-users")
    suspend fun getUsers(): List<UserDto>

    @POST("/api/v1/create-user")
    suspend fun createUser(@Body userDto: UserDto)

    @PATCH("/api/v1/update-user/{user_id}")
    suspend fun updateUser(@Path("user_id") itemId: String, @Body userDto: UserDto)

    @DELETE("/api/v1/delete-user/{user_id}")
    suspend fun deleteUser(@Path("user_id") itemId: String)



}






















