package com.example.socialnetwork.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeFragment : MvpAppCompatFragment(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE

        var authUser = arguments?.getParcelable<User>("authUser")

        if (authUser == null) {
            dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
                .addOnSuccessListener { document ->
                    authUser = User(
                        document.id,
                        document.data!!["full_name"] as String,
                        document.data!!["user_name"] as String,
                        document.data!!["picture"] as String
                    )
                    presenter.setListener(authUser!!)
                }

        } else {
            presenter.setListener(authUser!!)
        }

        authUserImageView.setOnClickListener {
            val bundle = bundleOf("authUser" to authUser)
            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment, bundle)
        }
    }


    override fun setAdapter(postAdapter: PostAdapter) {
        postListHomeRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = postAdapter
        }
    }
}
