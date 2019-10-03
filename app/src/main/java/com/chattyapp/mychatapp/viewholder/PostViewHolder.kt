package com.chattyapp.mychatapp.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.chattyapp.mychatapp.R
import com.chattyapp.mychatapp.data.Post
import kotlinx.android.synthetic.main.include_post_author.view.*
import kotlinx.android.synthetic.main.include_post_text.view.*
import kotlinx.android.synthetic.main.item_post.view.*

class PostViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    fun bindToPost(post: Post, startClickListener: View.OnClickListener) {
        itemView.postTitle.text = post.title
        itemView.postAuthor.text = post.author.username
        itemView.postNumStars.text = post.starCount.toString()
        itemView.postBody.text = post.body

        Glide.with(itemView.postAuthorPhoto.context).load(post.author.photoUrl).circleCrop()
            .override(Target.SIZE_ORIGINAL).into(itemView.postAuthorPhoto)

        itemView.star.setOnClickListener(startClickListener)
    }

    fun setLikedState(liked: Boolean) {
        when {
            liked -> itemView.star.setImageResource(R.drawable.ic_toggle_star_24)
            else -> itemView.star.setImageResource(R.drawable.ic_toggle_star_outline_24)
        }
    }
}