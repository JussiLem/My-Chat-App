package com.example.mychatapp.fragment

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class RecentPostFragment : PostListFragment() {
    override fun getQuery(databaseReference: DatabaseReference): Query {
        return databaseReference.child("posts")
            .limitToFirst(100)
    }
}