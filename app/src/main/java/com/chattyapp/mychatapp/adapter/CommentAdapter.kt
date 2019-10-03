package com.chattyapp.mychatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chattyapp.mychatapp.R
import com.chattyapp.mychatapp.data.Comment
import com.chattyapp.mychatapp.viewholder.CommentViewHolder
import com.chattyapp.timber.Timber
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.util.ArrayList

class CommentAdapter (
    private val context: Context,
    private val databaseReference: DatabaseReference
) : RecyclerView.Adapter<CommentViewHolder>() {

    private val childEventListener: ChildEventListener?

    internal val commentIds = ArrayList<String>()
    internal val comments = ArrayList<Comment>()

    init {

        // Create child event listener
        // [START child_event_listener_recycler]
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Timber.d { "onChildAdded: + ${dataSnapshot.key} " }

                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue(Comment::class.java)

                // [START_EXCLUDE]
                // Update RecyclerView
                commentIds.add(dataSnapshot.key ?: return)
                comments.add(comment ?: return)
                notifyItemInserted(comments.size - 1)
                // [END_EXCLUDE]
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                Timber.d { "onChildChanged: ${dataSnapshot.key}" }

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue(Comment::class.java)
                val commentKey = dataSnapshot.key

                // [START_EXCLUDE]
                val commentIndex = commentIds.indexOf(commentKey)
                if (commentIndex > -1 && newComment != null) {
                    // Replace with the new data
                    comments[commentIndex] = newComment

                    // Update the RecyclerView
                    notifyItemChanged(commentIndex)
                } else {
                    Timber.d { "onChildChanged:unknown_child: $commentKey" }
                }
                // [END_EXCLUDE]
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Timber.d { "onChildRemoved:" + dataSnapshot.key }

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // [START_EXCLUDE]
                val commentIndex = commentIds.indexOf(commentKey)
                if (commentIndex > -1) {
                    // Remove data from the list
                    commentIds.removeAt(commentIndex)
                    comments.removeAt(commentIndex)

                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex)
                } else {
                    Timber.d { "onChildRemoved:unknown_child:${commentKey!!}" }
                }
                // [END_EXCLUDE]
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Timber.d { "onChildMoved:${dataSnapshot.key}" }

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                dataSnapshot.getValue(Comment::class.java)
                dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d { "postComments:onCancelled ${databaseError.toException()}" }
                Toast.makeText(
                    context, "Failed to load comments.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        databaseReference.addChildEventListener(childEventListener)
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        this.childEventListener = childEventListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bindToComment(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    fun cleanupListener() {
        childEventListener?.let {
            databaseReference.removeEventListener(it)
        }
    }
}