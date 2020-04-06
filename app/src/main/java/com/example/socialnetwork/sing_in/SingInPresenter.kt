package com.example.socialnetwork.sing_in

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.retrofit.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@InjectViewState
class SingInPresenter : MvpPresenter<SingInView>() {

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (dbAuth.currentUser != null) {
            viewState.goToUserPage()
        }
    }

    fun singInUser(email: String, password: String) {
        dbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    viewState.goToUserPage()
                else
                    viewState.showToast("No user with such email and password")

            }
    }

    @SuppressLint("CheckResult")
    private fun uploadUsers() {
        Timber.plant(Timber.DebugTree())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val service = retrofit.create(Service::class.java)

        service.addUsers(10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                for (user in result.userRetrofitList) {
                    dbAuth.createUserWithEmailAndPassword(user.email, user.login.password)
                        .addOnCompleteListener {
                            Timber.d("${user.email}:  ${user.login.password}")
                            dbAuth.signInWithEmailAndPassword(user.email, user.login.password)
                            val userId = dbAuth.currentUser!!.uid
                            dbFirestore.collection("users").document(userId)
                                .set(
                                    hashMapOf(
                                        "full_name" to "${user.name.first} ${user.name.last}",
                                        "user_name" to user.login.username,
                                        "picture" to user.picture.medium,
                                        "following" to mutableListOf<String>()
                                    )
                                )
                        }

                }

            }
    }

}