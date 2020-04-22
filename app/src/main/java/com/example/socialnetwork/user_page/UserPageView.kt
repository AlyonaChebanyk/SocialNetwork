package com.example.socialnetwork.user_page

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User

interface UserPageView: MvpView {

    fun displayUserData(user: User)
    fun setAdapter(postAdapter: PostAdapter)
    fun setCheckedChip(check: Boolean)
    fun setListenerToWriteMessageButton()
    fun setListenerToFollowUserChip()
    fun setListenerToGoToMainPageButton()
    fun scrollToPosition(position: Int)
}