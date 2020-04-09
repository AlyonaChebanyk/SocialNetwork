package com.example.socialnetwork.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.activity_main.*

class UserProfileFragment : MvpAppCompatFragment(), UserProfileView {

    @InjectPresenter
    lateinit var presenter: UserProfilePresenter

    private val bottomSheet = AddPostBottomSheet()
    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()

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
                    presenter.displayUserData(authUser!!)
                    presenter.setListenerToUserPosts(authUser!!)
                }

        } else {
            presenter.displayUserData(authUser!!)
            presenter.setListenerToUserPosts(authUser!!)
        }


        addPostButton.setOnClickListener {
            val bundle = bundleOf("user" to authUser)
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
            .resize(280, 280)
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
