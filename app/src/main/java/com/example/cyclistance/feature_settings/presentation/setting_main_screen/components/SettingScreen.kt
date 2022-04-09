package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.theme.ThemeColor
import com.example.cyclistance.theme.editProfileTextFieldIndicatorColor

@Preview
@Composable
fun SettingScreen() {

    var checkedState by remember{ mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {

            SectionTitle(iconId = R.drawable.ic_baseline_person_24, title = "Account")
            Spacer(modifier = Modifier.height(7.dp))
            ButtonItem(buttonText = "Edit Profile")
            ButtonItem(buttonText = "Change Password")
        }

        Column(modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {


            SectionTitle(iconId = R.drawable.ic_baseline_brush_24, title = "Preference")
            DarkModeSwitchButton(
                checkedState = checkedState,
                onCheckedChange = {
                checkedState = it
            })

        }

        Column(modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {

            SectionTitle(iconId = R.drawable.ic_baseline_settings_suggest_24, title = "Other Settings")
            Spacer(modifier = Modifier.height(7.dp))
            ButtonItem(buttonText = "Privacy Policy")
            ButtonItem(buttonText = "Terms and Condition")
            ButtonItem(buttonText = "Rate this app")


        }


    }
}



