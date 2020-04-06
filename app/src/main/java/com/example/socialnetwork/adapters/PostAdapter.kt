package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User

class PostAdapter(private val goToUserPageByClick: Boolean = false) :
    RecyclerView.Adapter<PostViewHolder>() {

    private val postList: ArrayList<Post> = arrayListOf()

    fun addPost(post: Post){
        postList.add(0, post)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false) as View
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position], goToUserPageByClick)
    }

    override fun getItemCount(): Int = postList.size
}