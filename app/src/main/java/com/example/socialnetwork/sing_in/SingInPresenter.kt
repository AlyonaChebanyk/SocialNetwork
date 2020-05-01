package com.example.socialnetwork.sing_in

import android.content.Context
import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.main_activity.MainActivity
import com.example.socialnetwork.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@InjectViewState
class SingInPresenter : MvpPresenter<SingInView>() {

    private val dbAuth = FirebaseAuth.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()

    fun checkIfSingedIn(preferences: SharedPreferences){

        val isSingedIn = preferences.getBoolean("isSingedIn", false)

        if (isSingedIn){
            Repository.currentUser = getUserFromPreferences(preferences)
            viewState.goToUserPage()
        }

    }

    fun singInUser(email: String, password: String, preferences: SharedPreferences) {
        dbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
                        .addOnSuccessListener { document ->
                            val authUser = User(
                                document.id,
                                document.data!!["full_name"] as String,
                                document.data!!["user_name"] as String,
                                document.data!!["picture"] as String
                            )
                            Repository.currentUser = authUser

                            val editor = preferences.edit()
                            editor.putBoolean("isSingedIn", true)
                            editor.putString("id", authUser.id)
                            editor.putString("fullName", authUser.fullName)
                            editor.putString("userName", authUser.userName)
                            editor.putString("picture", authUser.picture)
                            editor.apply()

                            viewState.goToUserPage()
                        }
                else
                    viewState.showToast("No user with such email and password")
            }
    }

    private fun getUserFromPreferences(preferences: SharedPreferences): User{
        val id = preferences.getString("id", "")!!
        val fullName = preferences.getString("fullName", "")!!
        val userName = preferences.getString("userName", "")!!
        val picture = preferences.getString("picture", "")!!

        return User(id, fullName, userName, picture)
    }

}