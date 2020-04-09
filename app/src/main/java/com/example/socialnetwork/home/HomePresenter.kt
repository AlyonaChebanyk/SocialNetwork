package com.example.socialnetwork.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

@InjectViewState
class HomePresenter : MvpPresenter<HomeView>() {

    private val dbRealtime = FirebaseDatabase.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()
    private val adapter = PostAdapter(true)
    lateinit var user: User

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setAdapter(adapter)
    }

    fun setListener(user: User){
        val reference = dbRealtime.getReference("/following-posts/${user.id}")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                adapter.addPost(post!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

}
