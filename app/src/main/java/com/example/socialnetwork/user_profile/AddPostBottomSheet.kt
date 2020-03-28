package com.example.socialnetwork.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlin.collections.ArrayList

class AddPostBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbFirestore = FirebaseFirestore.getInstance()
        val dbRealtime = FirebaseDatabase.getInstance()

        val user = arguments!!.getSerializable("user") as User

        postButton.setOnClickListener {
            if (postEditText.text.isNotEmpty()) {
                val reference = dbRealtime.getReference("/user-posts/${user.id}").push()
                val userPost = Post(reference.key!!, user.id, postEditText.text.toString())
                reference.setValue(userPost)

                dbFirestore.collection("users").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val following = document.data["following"] as ArrayList<String>
                            if (following.contains(user.id)) {
                                val userId = document.id
                                val ref = dbRealtime.getReference("/following-posts/$userId").push()
                                ref.setValue(userPost)
                            }
                        }
                    }

                dialog?.hide()
                postEditText.text.clear()
            }

        }

    }

}