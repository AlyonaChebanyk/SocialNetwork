package com.example.socialnetwork.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_item_left_side.view.*
import kotlinx.android.synthetic.main.message_item_right_side.view.*

class ChatLogViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(message: Message, secondUser: User){
        val authUser = Repository.currentUser!!
        with(view){
            if (message.fromId == authUser.id){
                Picasso.get()
                    .load(authUser.picture)
                    .into(userRightImage)

                messageRightTextView.text = message.text
            }else{
                Picasso.get()
                    .load(secondUser.picture)
                    .into(userLeftImage)

                messageLeftTextView.text = message.text
            }
        }
    }
}