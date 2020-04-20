package com.example.socialnetwork.sing_in

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.example.socialnetwork.retrofit.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@InjectViewState
class SingInPresenter : MvpPresenter<SingInView>() {

    private val dbAuth = FirebaseAuth.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (dbAuth.currentUser != null) {
            goToUserPage()
        }
    }

    fun singInUser(email: String, password: String) {
        dbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    goToUserPage()
                else
                    viewState.showToast("No user with such email and password")

            }
    }

    private fun goToUserPage() {
        dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                val authUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String
                )
                Repository.currentUser = authUser
                viewState.goToUserPage()
            }
    }
}