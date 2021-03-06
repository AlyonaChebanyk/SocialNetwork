package com.example.socialnetwork.post_page

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.Comment
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_post_page.*
import java.util.*

@InjectViewState
class PostPagePresenter : MvpPresenter<PostPageView>(){

    private val dbRealtime = FirebaseDatabase.getInstance()
    private val adapter = CommentAdapter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setAdapter(adapter)
    }

    fun setListener(user: User, post: Post){
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

    fun addComment(commentText: String, user: User, post: Post){
        val ref = dbRealtime.getReference("/user-posts/${user.id}/${post.postId}/comments").push()
        val comment = Comment(post.postId, Repository.currentUser!!.id, commentText, Calendar.getInstance().timeInMillis)
        ref.setValue(comment)
        viewState.clearCommentText()
    }

    fun displayPostData(user: User, post: Post){
        viewState.displayPostData(user, post)
    }

}