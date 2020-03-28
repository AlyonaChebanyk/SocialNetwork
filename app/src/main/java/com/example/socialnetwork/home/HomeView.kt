package com.example.socialnetwork.home

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User

interface HomeView: MvpView {

    fun displayUserData(user: User)
    fun setAdapter(postAdapter: PostAdapter)

}