package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase


val userItems = listOf(
    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
        location = Location(latitude = 14.0874, longitude = 121.1517),
        name = "John Mark",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK9gqFKRn28xKHD1CAbEevdzsLmsv5yQkGnQ&usqp=CAU",
        rescueRequest = RescueRequest(
            listOf(
                Respondent(clientId = "2326236"),
                Respondent(clientId = "32362323"),
                Respondent(clientId = "423623")
            )
        )),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "92398512908",
        id = "2326236",
        location = Location(latitude = 14.079426870239514, longitude = 121.15672703265659),
        name = "Jane Dominic",
        profilePictureUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistance(
            needHelp = true,
            confirmationDetail = ConfirmationDetail(
                bikeType = "Road Bike",
                description = MappingConstants.BROKEN_CHAIN_TEXT,
                message = "I need help with my flat tire",
            ),
        ), transaction = Transaction(role = "rescuer", transactionId = "oinun98naksxnaiu2")),


    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "2845182739",
        id = "32362323",
        location = Location(latitude = 14.090421127354372, longitude = 121.14870941179073),
        name = "John Terminator",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistance(
            needHelp = true,
            confirmationDetail = ConfirmationDetail(
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
        location = Location(latitude = 14.096044763009578, longitude = 121.13918029795505),
        name = "Boomie",
        rescueRequest = RescueRequest(),
        userAssistance = UserAssistance(
            needHelp = true,
            confirmationDetail = ConfirmationDetail(
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
        location = Location(latitude = 14.044194, longitude = 121.747392),
        name = "John Doe",
        rescueRequest = RescueRequest(),
        profilePictureUrl = "https://www.erlanger.org/find-a-doctor/media/PhysicianPhotos/Carbone_1436.jpg",
        userAssistance = UserAssistance(
            needHelp = true,
            confirmationDetail = ConfirmationDetail(
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
        location = Location(latitude = 6.523497, longitude = 125.037057),
        name = "Jeniffer",
        profilePictureUrl = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "7",
        rescueRequest = RescueRequest(),
        location = Location(latitude = 6.531999, longitude = 125.043330),
        name = "Mark",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDwnb8KCy8eejkddV5FaKsBcm1uDznQhInOQ&usqp=CAU",
    ),

    UserItem(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "8",
        location = Location(latitude = 6.887962, longitude = 125.224905),
        userAssistance = UserAssistance(
            needHelp = true,
            confirmationDetail = ConfirmationDetail(
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
        location = Location(latitude = 7.118415, longitude = 125.283468),
        name = "John Dominic",
        profilePictureUrl = "https://www.commonwealthfund.org/sites/default/files/images/___media_upload_young_adults_individual_mandate_exemption_could_lead_to_more_uninsurance.jpg",
    ),

    )

suspend fun MappingUseCase.createMockUsers() {
    userItems.forEach {
        createUserUseCase(it)
    }
}