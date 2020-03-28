package com.example.socialnetwork.post_page

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_post_page.*

@InjectViewState
class PostPagePresenter : MvpPresenter<PostPageView>(){

    private val dbRealtime = FirebaseDatabase.getInstance()
    private lateinit var user: User
    private lateinit var post: Post
    lateinit var adapter: CommentAdapter

    fun setUser_(user: User){
        this.user = user
        adapter = CommentAdapter(user)
    }

    fun setPost_(post: Post){
        this.post = post
    }

    fun setListener(){
        val reference = dbRealtime.getReference("/user-posts/${user.id}/${post.postId}/comments")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val comment = p0.getValue(Comment::class.java)
                adapter.addComment(comment!!)
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    fun addComment(commentText: String){
        val ref = dbRealtime.getReference("/user-posts/${user.id}/${post.postId}/comments").push()
        val comment = Comment(post.postId, commentText)
        ref.setValue(comment)
        viewState.clearCommentText()
    }

}