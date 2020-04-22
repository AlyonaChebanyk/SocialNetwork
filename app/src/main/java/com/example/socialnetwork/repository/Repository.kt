package com.example.socialnetwork.repository

import android.annotation.SuppressLint
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

object Repository {
    var currentUser: User? = null

    private val dbAuth = FirebaseAuth.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("CheckResult")
    fun uploadUsers(amount: Int = 10) {
        Timber.plant(Timber.DebugTree())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val service = retrofit.create(Service::class.java)

        service.addUsers(amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                for (user in result.userRetrofitList) {
                    dbAuth.createUserWithEmailAndPassword(user.email, user.login.password)
                        .addOnSuccessListener {
                            Timber.d("${user.email}:  ${user.login.password}")
                            dbAuth.signInWithEmailAndPassword(user.email, user.login.password)
                            val userId = dbAuth.currentUser!!.uid

                            dbFirestore.collection("users").document(userId)
                                .set(
                                    hashMapOf(
                                        "full_name" to "${user.name.first} ${user.name.last}",
                                        "user_name" to user.login.username,
                                        "picture" to user.picture.medium,
                                        "following" to arrayListOf<String>()
                                    )
                                )
                        }

                }

            }
    }
}