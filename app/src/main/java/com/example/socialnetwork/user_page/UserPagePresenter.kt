package com.example.socialnetwork.user_page

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
import kotlinx.android.synthetic.main.fragment_user_page.*

@InjectViewState
class UserPagePresenter : MvpPresenter<UserPageView>() {

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbRealtime = FirebaseDatabase.getInstance()
    lateinit var authUser: User
    val adapter = PostAdapter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setAdapter(adapter)
    }

    fun displayUserData(userCurrentPage: User) {
        viewState.displayUserData(userCurrentPage)
    }

    fun setListener(userCurrentPage: User) {
        val reference = dbRealtime.getReference("/user-posts/${userCurrentPage.id}")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val post = p0.getValue(Post::class.java)
                adapter.addPost(post!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }
        )
    }

    fun checkChipFollowed(userCurrentPage: User, authUser: User) {
        dbFirestore.collection("users").document(authUser.id).get()
            .addOnSuccessListener { document ->
                val following = document.data!!["following"] as ArrayList<String>
                if (following.contains(userCurrentPage.id)) {
                    viewState.setCheckedChip(true)
                    dbFirestore.collection("users").document(authUser.id).set(
                        hashMapOf(
                            "full_name" to authUser.fullName,
                            "user_name" to authUser.userName,
                            "picture" to authUser.picture,
                            "following" to following
                        )
                    )
                }
            }
    }

    fun addToFollowings(userCurrentPage: User, authUser: User) {
        dbFirestore.collection("users").document(authUser.id).get()
            .addOnSuccessListener { document ->
                val following = document.data!!["following"] as ArrayList<String>
                following.add(userCurrentPage.id)
                dbFirestore.collection("users").document(authUser.id).set(
                    hashMapOf(
                        "full_name" to authUser.fullName,
                        "user_name" to authUser.userName,
                        "picture" to authUser.picture,
                        "following" to following
                    )
                )
            }

    }

    fun removeFromFollowings(userCurrentPage: User, authUser: User) {
        dbFirestore.collection("users").document(authUser.id).get()
            .addOnSuccessListener { document ->
                val following = document.data!!["following"] as ArrayList<String>
                following.remove(userCurrentPage.id)
                dbFirestore.collection("users").document(authUser.id).set(
                    hashMapOf(
                        "full_name" to authUser.fullName,
                        "user_name" to authUser.userName,
                        "picture" to authUser.picture,
                        "following" to following
                    )
                )
            }
    }
}