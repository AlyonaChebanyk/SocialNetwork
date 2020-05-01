package com.example.socialnetwork.sing_in

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.main_activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sing_in.*

class SingInFragment : MvpAppCompatFragment(), SingInView {

    @InjectPresenter
    lateinit var presenter: SingInPresenter
    lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pref = (activity as MainActivity).getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)

        presenter.checkIfSingedIn(pref)

        requireActivity().bottom_navigation.visibility = View.GONE
        requireActivity().toolbar.visibility = View.GONE

        return inflater.inflate(R.layout.fragment_sing_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        singInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            presenter.singInUser(email, password, pref)

        }

    }

    override fun goToUserPage() {
        findNavController().navigate(R.id.action_singInFragment_to_userProfileFragment)
    }

    override fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}
