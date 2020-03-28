package com.example.socialnetwork.search

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.SearchAdapter
import com.example.socialnetwork.entities.User

interface SearchView: MvpView {

    fun displayUserImage(user: User)
    fun setAdapter(searchAdapter: SearchAdapter)

}