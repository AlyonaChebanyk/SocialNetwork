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
import com.example.socialnetwork.for_round_image.CircleTransform

import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeFragment : MvpAppCompatFragment(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE

        val authUser = arguments!!.getParcelable<User>("authUser")!!

        presenter.setListener(authUser)

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
