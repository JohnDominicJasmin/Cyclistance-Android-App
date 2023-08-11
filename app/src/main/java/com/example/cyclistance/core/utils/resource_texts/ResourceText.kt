package com.example.cyclistance.core.utils.resource_texts

sealed class ResourceText{
    data class FieldLeftBlank(val message: String = "Field cannot be left blank"): ResourceText()
    data class EmailIsInvalid(val message: String = "Email is Invalid."): ResourceText()
    data class PasswordNotMatch(val message: String = "The specified password do not match."): ResourceText()
    data class PasswordWeak(val message: String = "Password must contain at least 8 characters, including numbers or special characters."): ResourceText()
}
