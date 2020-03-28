package com.example.socialnetwork.user_profile

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User

interface UserProfileView: MvpView {

    fun displayUserData(user: User)
    fun setAdapter(postAdapter: PostAdapter)

}