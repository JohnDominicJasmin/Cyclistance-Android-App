package com.example.cyclistance.core.presentation.dialogs.privacy_policy_dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun PrivacyPolicyDialog(
    modifier: Modifier,
    onDismiss: () -> Unit = {},
    onClickLink: (String) -> Unit = {},
    onClickAgree: () -> Unit = {}) {

    var dialogOpen by rememberSaveable { mutableStateOf(true) }

    val context = LocalContext.current
    if (dialogOpen) {
        Dialog(
            onDismissRequest = {
                onDismiss()
                dialogOpen = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true),
        ) {

            Surface(
                modifier = modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 4.dp,
                shape = RoundedCornerShape(4.dp),
            ) {


                Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 24.dp)) {
                    Text(
                        text = "Please agree to our terms",
                        style = MaterialTheme.typography.h6,
                        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
                            append("By joining, you agree to the ")
                        }

                        pushStringAnnotation(
                            tag = "policy",
                            annotation = context.getString(R.string.privacy_policy_url))

                        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                            append("privacy policy")
                        }
                        pop()

                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
                            append(" and ")
                        }


                        pushStringAnnotation(
                            tag = "terms",
                            annotation = context.getString(R.string.terms_and_condition_url))

                        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                            append("terms of use")
                        }

                        pop()
                    }

                    ClickableText(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = annotatedString,
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "policy",
                                start = offset,
                                end = offset).firstOrNull()?.let {
                                onClickLink(it.item)
                            }

                            annotatedString.getStringAnnotations(
                                tag = "terms",
                                start = offset,
                                end = offset).firstOrNull()?.let {
                                onClickLink(it.item)
                            }
                        })


                    Button(
                        onClick = onClickAgree, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp), shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = "Agree and Sign Up",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.button)
                    }


                }
            }

        }


    }
}


@Preview(device = "id:Galaxy Nexus", name = "Dark Theme")
@Composable
fun PreviewPrivacyPolicyDialog1() {

    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            PrivacyPolicyDialog(modifier = Modifier, onDismiss = {})
        }
    }
}


@Preview(device = "id:Galaxy Nexus", name = "Light Theme")
@Composable
fun PreviewPrivacyPolicyDialog2() {

    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            PrivacyPolicyDialog(modifier = Modifier, onDismiss = {}, )
        }
    }
}


