package com.example.socialnetwork.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.for_round_image.CircleTransform
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.main_activity.*

class UserProfileFragment : MvpAppCompatFragment(), UserProfileView {

    @InjectPresenter
    lateinit var presenter: UserProfilePresenter
    private val bottomSheet = AddPostBottomSheet()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.VISIBLE
        activity!!.bottom_navigation.visibility = View.VISIBLE

        addPostButton.setOnClickListener {
            val bundle = bundleOf("user" to presenter.getUser())
            bottomSheet.arguments = bundle
            if (bottomSheet.isAdded) {
                bottomSheet.dialog?.show()
            } else {
                bottomSheet.show(activity!!.supportFragmentManager, "TAG")
            }
        }

    }

    override fun displayUserData(user: User) {
        Picasso.get()
            .load(user.picture)
            .resize(300, 300)
            .transform(
                CircleTransform(
                    15
                )
            )
            .into(userImage)

        userNameTextView.text = user.fullName
        userLoginTextView.text = "@" + user.userName
    }

    override fun setAdapter(postAdapter: PostAdapter) {
        postListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = postAdapter
        }
    }
}
