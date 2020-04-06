package com.example.socialnetwork.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.User

class CommentAdapter(val user: User): RecyclerView.Adapter<CommentViewHolder>() {

    private val commentList = arrayListOf<Comment>()

    fun addComment(comment: Comment){
        commentList.add(0, comment)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position], user)
    }

    override fun getItemCount(): Int = commentList.size
}