package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User

class LatestMessagesAdapter(val authUser: User) :
    RecyclerView.Adapter<LatestMessagesViewHolder>() {

    private val latestMessagesList: MutableList<Message> = arrayListOf()

    fun addMessage(message: Message){
        latestMessagesList.add(message)
        notifyItemInserted(latestMessagesList.size-1)
    }

    fun setMessageList(messageList: MutableList<Message>){
        latestMessagesList.clear()
        latestMessagesList.addAll(messageList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestMessagesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.latest_message_item, parent, false)
        return LatestMessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestMessagesViewHolder, position: Int) {
        holder.bind(latestMessagesList[position], authUser)
    }

    override fun getItemCount(): Int = latestMessagesList.size
}