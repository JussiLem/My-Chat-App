package com.chattyapp.mychatapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    fun getUid(): String? {
        return when (val user = FirebaseAuth.getInstance().currentUser) {
            null -> null
            else -> user.uid
        }
    }

    fun savePostReference(postKey: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("posts").child(postKey)
    }

    fun saveCommentsReference(postKey: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("posts-comments").child(postKey)
    }
}