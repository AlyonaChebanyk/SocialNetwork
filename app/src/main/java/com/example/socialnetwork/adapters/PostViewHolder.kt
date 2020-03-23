package com.example.socialnetwork.adapters

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.CircleTransform
import com.example.socialnetwork.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.post_item.view.*
import kotlinx.android.synthetic.main.post_item.view.userImage
import kotlinx.android.synthetic.main.post_item.view.userNameTextView

class PostViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    val db = FirebaseFirestore.getInstance()

    fun bind(post: Post, goToUserPageByClick: Boolean = false) {

        db.collection("users").document(post.userId).get()
            .addOnSuccessListener { doc ->
                val picture = doc.data!!["picture"] as String
                val fullName = doc.data!!["full_name"] as String
                with(view) {
                    Picasso.get()
                        .load(picture)
                        .resize(110, 110)
                        .transform(CircleTransform(0))
                        .into(userImage)

                    postContentTextView.text = post.content
                    userNameTextView.text = fullName
                    postDateTextView.text = post.date.toDate().toString()
                }
            }

        if (goToUserPageByClick) {
            view.post_item.setOnClickListener {
                db.collection("users").document(post.userId).get()
                    .addOnSuccessListener { document ->
                        val user = User(
                            document.id,
                            document.data!!["full_name"] as String,
                            document.data!!["user_name"] as String,
                            document.data!!["picture"] as String,
                            document.data!!["following"] as MutableList<String>
                        )

                        val bundle = bundleOf("user" to user)
                        view.findNavController()
                            .navigate(R.id.action_homeFragment_to_userPageFragment, bundle)
                    }
            }
        }

    }
}