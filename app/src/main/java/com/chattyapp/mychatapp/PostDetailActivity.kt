package com.chattyapp.mychatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chattyapp.mychatapp.adapter.CommentAdapter
import com.chattyapp.mychatapp.data.Comment
import com.chattyapp.mychatapp.data.Post
import com.chattyapp.mychatapp.data.User
import com.chattyapp.timber.Timber
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.include_post_author.*
import kotlinx.android.synthetic.main.include_post_text.*

class PostDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var postKey: String
    private var postListener: ValueEventListener? = null
    private var adapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG)
        Timber.d { "Activity Created" }
        setContentView(R.layout.activity_post_detail)

        // Get post key from intent
        postKey = intent.getStringExtra(EXTRA_POST_KEY)
            ?: throw IllegalArgumentException("Must pass EXTRA_POST_KEY")


        // Initialize Views
        buttonPostComment.setOnClickListener(this)
        recyclerPostComments.layoutManager = LinearLayoutManager(this)
    }

    public override fun onStart() {
        super.onStart()

        // Add value event listener to the post
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(Post::class.java)
                // [START_EXCLUDE]
                post?.let {
                    postAuthor.text = it.author.username
                    postTitle.text = it.title
                    postBody.text = it.body
                }
                // [END_EXCLUDE]
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Timber.d { "loadPost:onCancelled ${databaseError.toException()}" }
                // [START_EXCLUDE]
                Toast.makeText(
                    baseContext, "Failed to load post.",
                    Toast.LENGTH_SHORT
                ).show()
                // [END_EXCLUDE]
            }
        }
        FirebaseHelper.savePostReference(postKey).addValueEventListener(postListener)
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        this.postListener = postListener

        // Listen for comments
        recyclerPostComments.adapter = CommentAdapter(this, FirebaseHelper.saveCommentsReference(postKey))
    }

    public override fun onStop() {
        super.onStop()
        val postReference = FirebaseHelper.savePostReference(postKey)
        // Remove post value event listener
        postListener?.let {
            postReference.removeEventListener(it)
        }

        // Clean up comments listener
        adapter?.cleanupListener()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonPostComment -> postComment()
        }
    }

    private fun postComment() {
        val uid = FirebaseHelper.getUid()
        if (uid.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Toast.makeText(
                baseContext,
                R.string.error_fetch_user,
                Toast.LENGTH_SHORT
            ).show()
            hideKeyboard()
        } else {
            FirebaseDatabase.getInstance().reference.child("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get user information
                        val user = dataSnapshot.getValue(User::class.java) ?: return
                        // Get post key from intent
                        val postKey = intent.getStringExtra(EXTRA_POST_KEY)
                            ?: throw IllegalArgumentException("Must pass EXTRA_POST_KEY")
                        val commentsReference = FirebaseHelper.saveCommentsReference(postKey)
//                        val authorName = user.username

                        // Create new comment object
                        val commentText = fieldCommentText.text.toString()
                        val comment = Comment(uid, user, commentText)

                        // Push the comment, it will appear in the list
                        commentsReference.push().setValue(comment)

                        // Clear the field
                        fieldCommentText.text = null
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Timber.w { "loadPost:onCancelled + ${databaseError.toException()}" }
                        // [START_EXCLUDE]
                        Toast.makeText(
                            this@PostDetailActivity, "Failed to load post.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // [END_EXCLUDE]
                    }
                })

        }
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {

        private const val TAG = "PostDetailActivity"
        const val EXTRA_POST_KEY: String = "post_key"
    }
}
