package com.example.socialnetwork.adapters

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.for_round_image.CircleTransform
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.latest_message_item.view.*

class LatestMessagesViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    val dbFirestore = FirebaseFirestore.getInstance()
    val dbAuth = FirebaseAuth.getInstance()
    lateinit var displayUserId: String

    fun bind(message: Message){

        displayUserId = if (message.fromId != dbAuth.currentUser!!.uid)
            message.fromId
        else
            message.toId

        dbFirestore.collection("users").document(displayUserId).get()
            .addOnSuccessListener { document ->
                val user = User(document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String)

                with(view){
                    Picasso.get()
                        .load(user.picture)
                        .transform(CircleTransform())
                        .resize(130, 130)
                        .into(userImage)

                    userNameTextView.text = user.fullName
                    latestMessageTextView.text = message.text

                    latestMessageId.setOnClickListener {
                        val bundle = bundleOf("user" to user)
                        findNavController().navigate(R.id.action_latestMessagesFragment_to_chatLogFragment, bundle)
                    }
                }
            }

    }

}