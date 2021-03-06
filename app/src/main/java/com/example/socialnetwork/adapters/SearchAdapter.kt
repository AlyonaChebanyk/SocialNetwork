package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User

class SearchAdapter: RecyclerView.Adapter<SearchViewHolder>() {

    private val searchList = mutableListOf<User>()
    private val limit = 10

    fun clearList(){
        searchList.clear()
        notifyDataSetChanged()
    }

    fun addUser(user: User){
        searchList.add(user)
        notifyItemInserted(searchList.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemCount(): Int {
        return if(searchList.size > limit){
            limit
        } else {
            searchList.size
        }
    }
}