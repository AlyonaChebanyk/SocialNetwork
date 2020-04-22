package com.example.socialnetwork.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.DateToStringConverter
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_item.view.*
import java.util.*

class CommentViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(comment: Comment, user: User){
        with(view){
            Picasso.get()
                .load(user.picture)
                .into(userImageComment)

            userNameCommentTextView.text = user.fullName
            userLoginCommentTextView.text = "@" + user.userName
            commentTextView.text = comment.text
            commentDateTextView.text = DateToStringConverter.getDaysAgo(Date(comment.timestamp))
        }
    }
}