package com.example.cyclistance.feature_mapping_screen.data

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.*
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.RescueTransactionItemDto
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserItemDto
import retrofit2.http.*

interface CyclistanceApi {


    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserItemDto

    @GET("/api/v1/get-users")
    suspend fun getUsers(): UserDto

    @POST("/api/v1/create-user")
    suspend fun createUser(@Body userItemDto: UserItemDto)

    @PATCH("/api/v1/update-user/{user_id}")
    suspend fun updateUser(@Path("user_id") itemId: String, @Body userItemDto: UserItemDto)

    @DELETE("/api/v1/delete-user/{user_id}")
    suspend fun deleteUser(@Path("user_id") itemId: String)




    @GET("/api/v1/get-rescue-transaction-by-id/{rescue_transaction_id}")
    suspend fun getRescueTransactionById(userId: String): RescueTransactionItemDto

    @POST("/api/v1/create-rescue-transaction")
    suspend fun createRescueTransaction(@Body rescueTransactionItemDto: RescueTransactionItemDto)

    @PATCH("/api/v1/update-rescue-transaction/{rescue_transaction_id}")
    suspend fun updateRescueTransaction(@Path("rescue_transaction_id") itemId: String, @Body rescueTransactionItemDto: RescueTransactionItemDto)

    @DELETE("/api/v1/delete-rescue-transaction/{rescue_transaction_id}")
    suspend fun deleteRescueTransaction(@Path("rescue_transaction_id") itemId: String)


}






















