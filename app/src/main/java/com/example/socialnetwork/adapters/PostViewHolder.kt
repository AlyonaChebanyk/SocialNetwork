package com.example.socialnetwork.adapters

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.DateToStringConverter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.post_item.view.*
import kotlinx.android.synthetic.main.post_item.view.userImage
import kotlinx.android.synthetic.main.post_item.view.userNameTextView
import java.util.*

class PostViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    val db = FirebaseFirestore.getInstance()
    val dbAuth = FirebaseAuth.getInstance()

    fun bind(post: Post, goToUserPageByClick: Boolean = false) {

        db.collection("users").document(post.userId).get()
            .addOnSuccessListener { document ->
                val user = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String
                )

                val bundle = bundleOf("user" to user, "post" to post)

                with(view) {
                    Picasso.get()
                        .load(user.picture)
                        .into(userImage)

                    postContentTextView.text = post.content
                    userNameTextView.text = user.fullName
                    userLoginTextView.text = "@" + user.userName
                    postDateTextView.text = DateToStringConverter.getDaysAgo(Date(post.timestamp))
                    if (post.postImageUrl.isNotEmpty()) {
                        Picasso.get()
                            .load(post.postImageUrl)
                            .into(postImage)
                    }

                }

                view.setOnClickListener {
                    if (user.id == dbAuth.currentUser!!.uid) {
                        view.findNavController()
                            .navigate(R.id.action_userProfileFragment_to_postPage, bundle)
                    } else {
                        view.findNavController()
                            .navigate(R.id.action_userPageFragment_to_postPage, bundle)
                    }

                }

            }


    }
}