package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.booleanPreferencesKey

object EmergencyCallConstants {
    const val PHILIPPINE_RED_CROSS_PHOTO =
        "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/icons/philippine_red_cross.png"
    const val PHILIPPINE_RED_CROSS = "Philippine Red Cross"
    const val PHILIPPINE_RED_CROSS_NUMBER = "143"

    const val NATIONAL_EMERGENCY = "National Emergency Hotline"
    const val NATIONAL_EMERGENCY_PHOTO =
        "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/icons/ph%20logo%201.png"
    const val NATIONAL_EMERGENCY_NUMBER = "911"
    const val EMERGENCY_CALL_VM_STATE_KEY = "emergency_call_vm_state_key"
    const val ADD_EDIT_CONTACT_VM_STATE_KEY = "add_edit_contact_vm_state_key"

    val CONTACTS_PURPOSELY_DELETE_KEY = booleanPreferencesKey("contacts_purposely_delete_key")
    const val MAX_CONTACTS = 7
    const val DICE_BEAR_URL = "https://api.dicebear.com/6.x/shapes/png?seed="
    const val EDIT_CONTACT_ID = "contactId"
}