package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import com.example.cyclistance.R

sealed class IntroSliderScreenItem(
    val image: Int,
    val title: String,
    val description: String) {


    object LiveLocation : IntroSliderScreenItem(
        image = R.drawable.ic_live_location,
        title = "Live Location Updates",
        description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. \n" +
                "numquam blanditiis harum Quo neque error repudiandae fuga? Ipsa laudantium "
    )

    object HelpAndRescue : IntroSliderScreenItem(
        image = R.drawable.ic_help_and_rescue,
        title = "Help and Rescue",
        description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. \n" +
                "numquam blanditiis harum Quo neque error repudiandae fuga? Ipsa laudantium "
    )

    object RealTimeMessaging : IntroSliderScreenItem(
        image = R.drawable.ic_real_time_messaging,
        title = "Real time messaging",
        description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. \n" +
                "numquam blanditiis harum Quo neque error repudiandae fuga? Ipsa laudantium "
    )
}
