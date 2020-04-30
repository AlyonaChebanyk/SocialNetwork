package com.example.socialnetwork.user_profile

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

@InjectViewState
class UserProfilePresenter : MvpPresenter<UserProfileView>() {

    private val dbRealtime = FirebaseDatabase.getInstance()
    private val adapter = PostAdapter()
    private val authUser = Repository.currentUser!!

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setAdapter(adapter)
        displayUserData()
        setListenerToUserPosts()
        viewState.setListenerToAddPostButton()

    }

    private fun displayUserData(){
        viewState.displayUserData(authUser)
    }

    private fun setListenerToUserPosts(){
        val reference = dbRealtime.getReference("/user-posts/${authUser.id}")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                adapter.addPost(post!!)
                viewState.scrollToPosition(0)
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

}

