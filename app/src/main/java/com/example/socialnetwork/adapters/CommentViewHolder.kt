package com.example.socialnetwork.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.DateToStringConverter
import com.example.socialnetwork.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_item.view.*
import java.util.*

class CommentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val dbFirestore = FirebaseFirestore.getInstance()
    lateinit var currentUser: User
    fun bind(comment: Comment) {
        dbFirestore.collection("users").document(comment.userId).get()
            .addOnSuccessListener { document ->
                currentUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String
                )
                with(view) {
                    Picasso.get()
                        .load(currentUser.picture)
                        .into(userImageComment)

                    userNameCommentTextView.text = currentUser.fullName
                    userLoginCommentTextView.text = "@" + currentUser.userName
                    commentTextView.text = comment.text
                    commentDateTextView.text =
                        DateToStringConverter.getDaysAgo(Date(comment.timestamp))
                }
            }

    }
}