package com.example.socialnetwork.user_profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.CircleTransform
import com.example.socialnetwork.adapters.PostAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.main_activity.*
import timber.log.Timber
import kotlin.collections.ArrayList

class UserProfileFragment : Fragment(){

    private val bottomSheet = BottomSheet()
    private lateinit var myAdapter: PostAdapter
    lateinit var db: FirebaseFirestore
    lateinit var mAuth: FirebaseAuth
    private val postList: ArrayList<Post> = arrayListOf()
    lateinit var authUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.VISIBLE
        activity!!.bottom_navigation.visibility = View.VISIBLE

        Timber.plant(Timber.DebugTree())

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        db.collection("users").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                authUser = User(
                    mAuth.currentUser!!.uid as String,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String,
                    document.data!!["following"] as MutableList<String>
                )

                Timber.plant(Timber.DebugTree())

                Picasso.get()
                    .load(authUser.picture)
                    .resize(300, 300)
                    .transform(CircleTransform(15))
                    .into(userImage)

                userNameTextView.text = authUser.fullName
                userLoginTextView.text = "@" + authUser.userName

                db.collection("posts")
                    .whereEqualTo("user_id", mAuth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            postList.add(
                                Post(
                                    doc.data["user_id"] as String,
                                    doc.data["content"] as String,
                                    doc.data["date"] as Timestamp
                                )
                            )
                        }
                        Timber.d(postList.toString())
                        myAdapter =
                            PostAdapter(postList)
                        postListRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = myAdapter
                        }

                    }
            }

        addPostButton.setOnClickListener {
            if (bottomSheet.isAdded) {
                bottomSheet.dialog?.show()
            } else {
                bottomSheet.show(activity!!.supportFragmentManager, "TAG")
            }
        }

    }

    fun updatePostList() {
        postList.clear()
        db.collection("posts")
            .whereEqualTo("user_id", mAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    postList.add(
                        Post(
                            document.data["user_id"] as String,
                            document.data["content"] as String,
                            document.data["date"] as Timestamp
                        )
                    )
                }
                myAdapter.notifyDataSetChanged()

            }
    }


}
