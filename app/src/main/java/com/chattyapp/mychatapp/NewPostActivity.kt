package com.chattyapp.mychatapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chattyapp.mychatapp.data.Post
import com.chattyapp.mychatapp.data.User
import com.chattyapp.timber.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_new_post.*
import java.util.HashMap

class NewPostActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_post)
        Timber.tag(TAG)
        Timber.d { "Activity Created" }
        // [START initialize_database_ref]
        database = FirebaseDatabase.getInstance().reference
        // [END initialize_database_ref]

        fabSubmitPost.setOnClickListener { submitPost() }
    }

    private fun submitPost() {
        val title = fieldTitle.text.toString()
        val body = fieldBody.text.toString()

        // Title is required
        if (TextUtils.isEmpty(title)) {
            fieldTitle.error = REQUIRED
            return
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            fieldBody.error = REQUIRED
            return
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false)
        Toast.makeText(this, getString(R.string.posting), Toast.LENGTH_SHORT).show()

        // [START single_value_read]
        val userId = getUid()
        if (userId.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Toast.makeText(
                baseContext,
                "Error: could not fetch user.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            database.child("users").child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get user value
                        val user = dataSnapshot.getValue(User::class.java)

                        // [START_EXCLUDE]
                        when (user) {
                            null -> {
                                // User is null, error out
                                Timber.wtf { "User $userId is unexpectedly null" }
                                Toast.makeText(
                                    baseContext,
                                    getString(R.string.error_fetch_user),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> // Write new post
                                writeNewPost(userId, user, title, body)
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true)
                        finish()
                        // [END_EXCLUDE]
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Timber.w { "getUser:onCancelled + ${databaseError.toException()}" }
                        // [START_EXCLUDE]
                        setEditingEnabled(true)
                        // [END_EXCLUDE]
                    }
                })
            // [END single_value_read]
        }
    }

    private fun getUid(): String? {

        return FirebaseAuth.getInstance().currentUser?.uid
    }


    private fun setEditingEnabled(enabled: Boolean) {
        fieldTitle.isEnabled = enabled
        fieldBody.isEnabled = enabled
        if (enabled) {
            fabSubmitPost.show()
        } else {
            fabSubmitPost.hide()
        }
    }

    private fun writeNewPost(userId: String, username: User, title: String, body: String) {
        if (userId.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Error: could not fetch user.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = database.child("posts").push().key
        if (key == null) {
            Timber.w {  "Couldn't get push key for posts" }
            return
        }

        val post = Post(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/posts/$key"] = postValues
        childUpdates["/user-posts/$userId/$key"] = postValues

        database.updateChildren(childUpdates)
    }
    // [END write_fan_out]

    companion object {

        private const val TAG = "NewPostActivity"
        private const val REQUIRED = "Required"
    }
}
