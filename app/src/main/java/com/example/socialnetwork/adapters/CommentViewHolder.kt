package com.example.socialnetwork.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.for_round_image.CircleTransform
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(comment: Comment, user: User){
        with(view){
            Picasso.get()
                .load(user.picture)
                .resize(150, 150)
                .transform(CircleTransform())
                .into(userImageComment)

            userNameCommentTextView.text = user.userName
            userLoginCommentTextView.text = "@" + user.userName
            commentTextView.text = comment.text
        }
    }
}