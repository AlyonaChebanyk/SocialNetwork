package com.example.socialnetwork.sing_in

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.entities.User

interface SingInView: MvpView {

    fun goToUserPage()
    fun showToast(text: String)

}