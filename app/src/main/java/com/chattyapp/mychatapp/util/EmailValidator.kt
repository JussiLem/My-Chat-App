package com.chattyapp.mychatapp.util

import java.util.regex.Pattern

class EmailValidator {

    private var isValid = false

    fun afterTextChanged(editableText: String?): Boolean {
        isValid = isValidEmail(editableText)
        return isValidEmail(editableText)
    }

    companion object {
        private val EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun isValidEmail(email: CharSequence?): Boolean {
            return email != null && EMAIL_PATTERN.matcher(email).matches()
        }
    }
}