package com.example.cyclistance.feature_on_boarding.presentation.components

import com.example.cyclistance.R

sealed class IntroSliderScreenItems(
    val image: Int,
    val title: String,
    val description: String) {


    object LiveLocation : IntroSliderScreenItems(
        image = R.drawable.ic_live_location,
        title = "Live Location Updates",
        description = "You can now share your Live Location with someone. Use Live Location to coordinate meetups and ensure the safety of everyone."
    )

    object HelpAndRescue : IntroSliderScreenItems(
        image = R.drawable.ic_help_and_rescue,
        title = "Help and Rescue",
        description = "You can now search for cyclists who need your help and support. Keep in mind that safety is our top priority."
    )

    object RealTimeMessaging : IntroSliderScreenItems(
        image = R.drawable.ic_real_time_messaging,
        title = "Real time messaging",
        description = "Your messages can now be delivered in real time. Remember to review their specific details before taking action."
    )
}
