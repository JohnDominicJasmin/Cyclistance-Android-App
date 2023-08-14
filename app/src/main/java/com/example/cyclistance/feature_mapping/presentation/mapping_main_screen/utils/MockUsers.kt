package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.model.api.user.ConfirmationDetailModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.RescueRequest
import com.example.cyclistance.feature_mapping.domain.model.api.user.RespondentModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.TransactionModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.UserAssistanceModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.UserItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.BikeType


val userItems = listOf(
    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "V45BYRCwjSzb9nljb6aZ2W82VKi5",
        location = LocationModel(latitude = 14.0874, longitude = 121.1517),
        name = "John Mark",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK9gqFKRn28xKHD1CAbEevdzsLmsv5yQkGnQ&usqp=CAU",
        rescueRequest = RescueRequest(
            listOf(
                RespondentModel(clientId = "2326236"),
                RespondentModel(clientId = "32362323"),
                RespondentModel(clientId = "423623")
            )
        )),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "92398512908",
        id = "2326236",
        location = LocationModel(latitude = 14.079426870239514, longitude = 121.15672703265659),
        name = "Jane Dominic",
        profilePictureUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistanceModel(
            needHelp = true,
            confirmationDetail = ConfirmationDetailModel(
                bikeType = BikeType.RoadBike.type,
                description = MappingConstants.BROKEN_CHAIN_TEXT,
                message = "I need help with my flat tire",
            ),
        ), transaction = TransactionModel(role = "rescuer", transactionId = "oinun98naksxnaiu2")),


    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "2845182739",
        id = "32362323",
        location = LocationModel(latitude = 14.090421127354372, longitude = 121.14870941179073),
        name = "John Terminator",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistanceModel(
            needHelp = true,
            confirmationDetail = ConfirmationDetailModel(
                bikeType = "Road Bike",
                description = MappingConstants.FAULTY_BRAKES_TEXT,
                message = "I need help with my flat tire",
            ),
        ),
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSv9zzOmzF32TNcQ2O93T21Serg2aJj5O-1hrQdZiE6ITGiKLsW4rjgVpX-asQYXa4iVeA&usqp=CAU",
    ),
    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "2367238327",
        id = "423623",
        location = LocationModel(latitude = 14.08894440102451, longitude = 121.14033042983098),
        name = "Boomie",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistanceModel(
            needHelp = true,
            confirmationDetail = ConfirmationDetailModel(
                bikeType = "Road Bike",
                description = MappingConstants.INJURY_TEXT,
                message = "I need help with my flat tire",
            ),
        ),
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8E4cPH6JfDvMMFNuu_M3LC2gXHX-pE0ieN3yt4TF8qDsdVZrXU0wa18WgljS9cvMXAzk&usqp=CAU",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "5",
        location = LocationModel(latitude = 14.044194, longitude = 121.747392),
        name = "John Doe",
        rescueRequest = RescueRequest(),
        profilePictureUrl = "https://www.erlanger.org/find-a-doctor/media/PhysicianPhotos/Carbone_1436.jpg",
        userAssistance = UserAssistanceModel(
            needHelp = true,
            confirmationDetail = ConfirmationDetailModel(
                bikeType = "Road Bike",
                description = MappingConstants.BROKEN_FRAME_TEXT,
                message = "I need help with my flat tire",
            ),
        ),
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "6",
        rescueRequest = RescueRequest(),
        location = LocationModel(latitude = 6.523497, longitude = 125.037057),
        name = "Jeniffer",
        profilePictureUrl = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "7",
        rescueRequest = RescueRequest(),
        location = LocationModel(latitude = 6.531999, longitude = 125.043330),
        name = "Mark",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDwnb8KCy8eejkddV5FaKsBcm1uDznQhInOQ&usqp=CAU",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "8",
        location = LocationModel(latitude = 6.887962, longitude = 125.224905),
        userAssistance = UserAssistanceModel(
            needHelp = true,
            confirmationDetail = ConfirmationDetailModel(
                bikeType = "Road Bike",
                description = MappingConstants.BROKEN_CHAIN_TEXT,
                message = "I need help with my flat tire",
            ),
        ),
        name = "Alex",
        profilePictureUrl = "https://thumbs.dreamstime.com/b/young-indian-man-having-fun-doing-video-call-outdoor-home-garden-mobile-phone-happy-person-using-technology-trends-tech-181375754.jpg",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "9",
        location = LocationModel(latitude = 7.118415, longitude = 125.283468),
        name = "John Dominic",
        profilePictureUrl = "https://www.commonwealthfund.org/sites/default/files/images/___media_upload_young_adults_individual_mandate_exemption_could_lead_to_more_uninsurance.jpg",
    ),

    )

suspend fun MappingUseCase.createMockUsers() {
    userItems.forEach {
        createUserUseCase(it)
    }
}