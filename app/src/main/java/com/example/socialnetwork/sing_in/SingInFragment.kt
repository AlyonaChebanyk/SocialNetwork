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
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R

import com.example.socialnetwork.entities.User
import com.example.socialnetwork.retrofit.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sing_in.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class SingInFragment : MvpAppCompatFragment(), SingInView {

    @InjectPresenter
    lateinit var presenter: SingInPresenter

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sing_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        singInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            presenter.singInUser(email, password)

        }

    }

    override fun goToUserPage() {
            dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
                .addOnSuccessListener { document ->
                    val authUser = User(
                        document.id,
                        document.data!!["full_name"] as String,
                        document.data!!["user_name"] as String,
                        document.data!!["picture"] as String
                    )
                    val bundle = bundleOf("authUser" to authUser)
                    findNavController().navigate(R.id.action_singInFragment_to_userProfileFragment, bundle)
                }

    }

    override fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}
