package com.example.socialnetwork.sing_in

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.R

import com.example.socialnetwork.entities.User
import com.example.socialnetwork.retrofit.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sing_in.*
import kotlinx.android.synthetic.main.main_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class SingInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_sing_in, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE
        activity!!.bottom_navigation.visibility = View.GONE

        Timber.plant(Timber.DebugTree())

        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null)
            findNavController().navigate(R.id.action_singInFragment_to_userProfileFragment)


        if (false) {
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
                        mAuth.createUserWithEmailAndPassword(user.email, user.login.password)
                            .addOnCompleteListener {
                                Timber.d("${user.email}:  ${user.login.password}")
                                mAuth.signInWithEmailAndPassword(user.email, user.login.password)
                                val userId = mAuth.currentUser!!.uid
                                db.collection("users").document(userId)
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

        singInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_singInFragment_to_userProfileFragment)
                    } else {
                        Toast.makeText(
                            activity,
                            "No user with such email and password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

    }
}
