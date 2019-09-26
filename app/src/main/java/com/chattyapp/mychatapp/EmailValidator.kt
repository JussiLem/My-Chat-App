package com.chattyapp.mychatapp


import java.util.regex.Pattern

class EmailValidator {

    private var isValid = false

    fun afterTextChanged(editableText: String?): String? {
        isValid = isValidEmail(editableText)
        return if (isValid) {
            editableText
        } else {
            null
        }

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