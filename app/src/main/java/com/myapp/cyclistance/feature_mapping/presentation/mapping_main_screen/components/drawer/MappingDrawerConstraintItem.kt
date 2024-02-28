package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.drawer

sealed class MappingDrawerConstraintItem(val layoutId: String) {

    object UpperSection : MappingDrawerConstraintItem(layoutId = "upper_section")
    object BottomSection : MappingDrawerConstraintItem(layoutId = "bottom_section")

}
