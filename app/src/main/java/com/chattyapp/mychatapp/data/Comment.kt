package com.chattyapp.mychatapp.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Comment(
    var uid: String? = "",
    var author: String? = "",
    var text: String? = ""
)