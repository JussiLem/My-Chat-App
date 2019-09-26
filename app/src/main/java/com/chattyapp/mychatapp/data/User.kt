package com.chattyapp.mychatapp.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val username: String = "",
    val email: String = "",
    var photoUrl: String = ""
)