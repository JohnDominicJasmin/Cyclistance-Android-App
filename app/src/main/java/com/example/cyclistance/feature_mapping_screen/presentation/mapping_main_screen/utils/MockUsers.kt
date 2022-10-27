package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.*
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase


val users = listOf(
    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "1",
        location = Location(lat = 14.599512, lng = 120.984222),
        name = "John Doe",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK9gqFKRn28xKHD1CAbEevdzsLmsv5yQkGnQ&usqp=CAU",
        rescueRequest = RescueRequest(
            listOf(
                Respondent(
                    clientId = "2",
                    requestAccepted = false
                ),
                Respondent(
                    clientId = "3",
                    requestAccepted = false
                ),
                Respondent(
                    clientId = "4",
                    requestAccepted = false
                )
            )
        ),
        userAssistance = UserAssistance(
            confirmationDetails = ConfirmationDetails(
                bikeType = "Road Bike",
                description = "Flat tire",
                message = "I need help with my flat tire",
            ),
            status = Status(
                started = true,
                ongoing = true
            )
        ),
        userNeededHelp = true),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "2",
        location = Location(lat = 14.628978, lng = 121.252176),
        name = "Jane Doe",
        userNeededHelp = true,
        profilePictureUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
    ),
    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "3",
        location = Location(lat = 14.208007, lng = 121.429968),
        name = "John Doe",
        userNeededHelp = true,
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSv9zzOmzF32TNcQ2O93T21Serg2aJj5O-1hrQdZiE6ITGiKLsW4rjgVpX-asQYXa4iVeA&usqp=CAU",
    ),
    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "4",
        location = Location(lat = 13.823594, lng = 121.269195),
        name = "Jane Doe",
        userNeededHelp = true,
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8E4cPH6JfDvMMFNuu_M3LC2gXHX-pE0ieN3yt4TF8qDsdVZrXU0wa18WgljS9cvMXAzk&usqp=CAU",
    ),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "5",
        location = Location(lat = 14.044194, lng = 121.747392),
        name = "John Doe",
        profilePictureUrl = "https://www.erlanger.org/find-a-doctor/media/PhysicianPhotos/Carbone_1436.jpg",
    ),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "6",
        location = Location(lat = 6.523497, lng = 125.037057),
        name = "Jeniffer",
        profilePictureUrl = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
    ),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "7",
        location = Location(lat = 6.531999, lng = 125.043330),
        name = "Mark",
        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDwnb8KCy8eejkddV5FaKsBcm1uDznQhInOQ&usqp=CAU",
    ),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "8",
        location = Location(lat = 6.887962, lng = 125.224905),
        name = "Alex",
        profilePictureUrl = "https://thumbs.dreamstime.com/b/young-indian-man-having-fun-doing-video-call-outdoor-home-garden-mobile-phone-happy-person-using-technology-trends-tech-181375754.jpg",
    ),

    User(
        address = "1234 Main Street, New York, NY 10001",
        contactNumber = "1234567890",
        id = "9",
        location = Location(lat = 7.118415, lng = 125.283468),
        name = "John Dominic",
        profilePictureUrl = "https://www.commonwealthfund.org/sites/default/files/images/___media_upload_young_adults_individual_mandate_exemption_could_lead_to_more_uninsurance.jpg",
    ),

)

suspend fun MappingUseCase.createMockUsers(){
    users.forEach {
        createUserUseCase(it)
    }
}