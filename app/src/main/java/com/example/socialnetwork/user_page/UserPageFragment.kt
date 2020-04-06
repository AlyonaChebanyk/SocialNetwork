package com.example.socialnetwork.user_page

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
import kotlinx.android.synthetic.main.fragment_user_page.*
import kotlinx.android.synthetic.main.fragment_user_profile.postListRecyclerView
import kotlinx.android.synthetic.main.fragment_user_profile.userImage
import kotlinx.android.synthetic.main.fragment_user_profile.userLoginTextView
import kotlinx.android.synthetic.main.fragment_user_profile.userNameTextView

class UserPageFragment : MvpAppCompatFragment(), UserPageView {

    @InjectPresenter
    lateinit var presenter: UserPagePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authUser = arguments!!.getParcelable<User>("authUser")!!
        val userCurrentPage = arguments!!.getParcelable<User>("user")!!

        presenter.displayUserData(userCurrentPage)
        presenter.setListener(userCurrentPage)
        presenter.checkChipFollowed(userCurrentPage, authUser)

        followUserChip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                presenter.addToFollowings(userCurrentPage, authUser)
            } else {
                presenter.removeFromFollowings(userCurrentPage, authUser)
            }

        }

        writeMessageButton.setOnClickListener {
            val bundle = bundleOf("authUser" to authUser, "user" to userCurrentPage)
            findNavController().navigate(R.id.action_userPageFragment_to_chatLogFragment, bundle)
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

    override fun setCheckedChip(check: Boolean) {
        followUserChip.isChecked = check
    }
}
