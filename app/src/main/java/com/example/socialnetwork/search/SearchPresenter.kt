package com.example.socialnetwork.search

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.SearchAdapter
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@InjectViewState
class SearchPresenter : MvpPresenter<SearchView>() {

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()
    private lateinit var adapter: SearchAdapter
    private val fullUserList = arrayListOf<User>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        dbFirestore.collection("users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val currentUser = User(
                        document.id,
                        document.data["full_name"] as String,
                        document.data["user_name"] as String,
                        document.data["picture"] as String
                    )
                    if (currentUser.id != dbAuth.currentUser!!.uid){
                        fullUserList.add(currentUser)
                        adapter.addUser(currentUser)
                    }

                }
            }

    }

    fun setAdapter(authUser: User){
        adapter = SearchAdapter(authUser)
        viewState.setAdapter(adapter)
    }

    fun updateSearchList(str: String){
        adapter.clearList()
        for(user in fullUserList){
            if (user.fullName.contains(str, true) || user.userName.contains(str, true))
                adapter.addUser(user)
        }
    }
}