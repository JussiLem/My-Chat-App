package com.chattyapp.mychatapp.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chattyapp.mychatapp.data.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindToComment(comment: Comment) {
        itemView.commentAuthor.text = comment.author.username
        itemView.commentBody.text = comment.text
    }
}