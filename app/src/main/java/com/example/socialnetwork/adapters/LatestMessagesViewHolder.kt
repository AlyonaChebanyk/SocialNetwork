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

    val db = FirebaseFirestore.getInstance()
    val dbAuth = FirebaseAuth.getInstance()
    lateinit var displayUserId: String

    fun bind(message: Message){

        if (message.fromId != dbAuth.currentUser!!.uid)
            displayUserId = message.fromId
        else
            displayUserId = message.toId

        db.collection("users").document(displayUserId).get()
            .addOnSuccessListener { document ->
                val user = User(document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String,
                    document.data!!["following"] as ArrayList<String>)

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
//
//
//        if (message.fromId != dbAuth.currentUser!!.uid){
//            db.collection("users").document(message.fromId).get()
//                .addOnSuccessListener { document ->
//                    val user = User(document.id,
//                        document.data!!["full_name"] as String,
//                        document.data!!["user_name"] as String,
//                        document.data!!["picture"] as String,
//                        document.data!!["following"] as ArrayList<String>)
//
//                    with(view){
//                        Picasso.get()
//                            .load(user.picture)
//                            .transform(CircleTransform())
//                            .resize(130, 130)
//                            .into(userImage)
//
//                        userNameTextView.text = user.fullName
//                        latestMessageTextView.text = message.text
//
//                        latestMessageId.setOnClickListener {
//                            val bundle = bundleOf("user" to user)
//                            findNavController().navigate(R.id.action_latestMessagesFragment_to_chatLogFragment, bundle)
//                        }
//                    }
//                }
//
//
//        }else{
//            db.collection("users").document(message.toId).get()
//                .addOnSuccessListener { document ->
//                    val user = User(document.id,
//                        document.data!!["full_name"] as String,
//                        document.data!!["user_name"] as String,
//                        document.data!!["picture"] as String,
//                        document.data!!["following"] as ArrayList<String>)
//
//                    with(view){
//                        Picasso.get()
//                            .load(user.picture)
//                            .transform(CircleTransform())
//                            .resize(130, 130)
//                            .into(userImage)
//
//                        userNameTextView.text = user.fullName
//                        latestMessageTextView.text = "You: " + message.text
//
//                        latestMessageId.setOnClickListener {
//                            val bundle = bundleOf("user" to user)
//                            findNavController().navigate(R.id.action_latestMessagesFragment_to_chatLogFragment, bundle)
//                        }
//                    }
//                }
//
//        }

    }

}