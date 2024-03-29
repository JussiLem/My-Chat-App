package com.chattyapp.mychatapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    var uid: String? = "",
    var author: User = User("", "", ""),
    var title: String = "",
    var body: String = "",
    var starCount: Int = 0,
    var stars: MutableMap<String?, Boolean> = HashMap()
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "author" to author,
            "title" to title,
            "body" to body,
            "starCount" to starCount,
            "stars" to stars
        )
    }
}