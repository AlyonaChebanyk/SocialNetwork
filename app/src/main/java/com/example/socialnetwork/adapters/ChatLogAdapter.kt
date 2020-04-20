package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.google.firebase.firestore.FirebaseFirestore

class ChatLogAdapter(private val secondUser: User) :
    RecyclerView.Adapter<ChatLogViewHolder>() {

    private val messageList: ArrayList<Message> = arrayListOf()

    val LEFT_SIDE = 1
    val RIGHT_SIDE = 2

    fun addMessage(message: Message){
        messageList.add(message)
        notifyItemInserted(messageList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogViewHolder {
        val view: View
        return when(viewType){
            LEFT_SIDE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_left_side, parent, false)
                ChatLogViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_right_side, parent, false)
                ChatLogViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ChatLogViewHolder, position: Int) {
        holder.bind(messageList[position], secondUser)
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        return when(messageList[position].fromId){
            Repository.currentUser!!.id -> RIGHT_SIDE
            else -> LEFT_SIDE
        }
    }
}