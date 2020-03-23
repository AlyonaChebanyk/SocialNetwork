package com.example.socialnetwork.user_page

import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_user_page.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.postListRecyclerView
import kotlinx.android.synthetic.main.fragment_user_profile.userImage
import kotlinx.android.synthetic.main.fragment_user_profile.userLoginTextView
import kotlinx.android.synthetic.main.fragment_user_profile.userNameTextView

/**
 * A simple [Fragment] subclass.
 */
class UserPageFragment : Fragment() {

    lateinit var authUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()

        val user = arguments?.getSerializable("user") as User

        db.collection("users").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                authUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String,
                    document.data!!["following"] as MutableList<String>)

                if (authUser.following.contains(user.id)){
                    val following = authUser.following
                    followUserChip.isChecked = true
                    following.remove(user.id)
                    db.collection("users").document(authUser.id).set(
                        hashMapOf("full_name" to authUser.fullName,
                            "user_name" to authUser.userName,
                            "picture" to authUser.picture,
                            "following" to following))
                }
            }



        Picasso.get()
            .load(user.picture)
            .resize(300, 300)
            .transform(CircleTransform(15))
            .into(userImage)

        userNameTextView.text = user.fullName
        userLoginTextView.text = "@" + user.userName

        val postList = arrayListOf<Post>()

        db.collection("posts")
            .whereEqualTo("user_id", user.id)
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
                val myAdapter = PostAdapter(postList)
                postListRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = myAdapter
                }
            }

        followUserChip.setOnCheckedChangeListener { buttonView, isChecked ->
            val following = authUser.following
            if (isChecked){
                following.add(user.id)
                db.collection("users").document(authUser.id).set(
                    hashMapOf("full_name" to authUser.fullName,
                        "user_name" to authUser.userName,
                        "picture" to authUser.picture,
                        "following" to following))
            }else{
                following.remove(user.id)
                db.collection("users").document(authUser.id).set(
                    hashMapOf("full_name" to authUser.fullName,
                        "user_name" to authUser.userName,
                        "picture" to authUser.picture,
                        "following" to following))
            }

        }
    }
}
