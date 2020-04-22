package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User

class LatestMessagesAdapter() :
    RecyclerView.Adapter<LatestMessagesViewHolder>() {

    private val latestMessagesList: MutableList<Message> = arrayListOf()

    fun addMessage(message: Message){
        latestMessagesList.add(0, message)
        notifyItemInserted(0)
    }

    fun setMessageList(messageList: MutableList<Message>){
        latestMessagesList.clear()
        val sortedList = messageList.sortedWith(compareBy {it.timestamp})
        latestMessagesList.addAll(sortedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestMessagesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.latest_message_item, parent, false)
        return LatestMessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestMessagesViewHolder, position: Int) {
        holder.bind(latestMessagesList[position])
    }

    override fun getItemCount(): Int = latestMessagesList.size
}