package com.example.socialnetwork.sing_in

import com.arellomobile.mvp.MvpView

interface SingInView: MvpView {

    fun goToUserPage()
    fun showToast(text: String)

}