package com.example.socialnetwork.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialnetwork.CircleTransform

import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.PostAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.main_activity.*

class HomeFragment : Fragment() {

    lateinit var authUser: User
    lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE

        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val postList = arrayListOf<Post>()
        db.collection("users").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                authUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String,
                    document.data!!["following"] as MutableList<String>
                )

                Picasso.get()
                    .load(authUser.picture)
                    .transform(CircleTransform())
                    .resize(100, 100)
                    .into(authUserImageView)

                authUserImageView.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
                }

                db.collection("posts")
                    .whereIn("user_id", authUser.following).get()
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
                        postAdapter = PostAdapter(postList, true)
                        postListHomeRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = postAdapter
                        }
                    }
            }

        refreshButton.setOnClickListener {
            postList.clear()
            db.collection("posts")
                .whereIn("user_id", authUser.following).get()
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
                    postAdapter.notifyDataSetChanged()
                }
        }

    }
}
