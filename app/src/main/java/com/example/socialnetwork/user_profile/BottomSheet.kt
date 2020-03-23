package com.example.socialnetwork.user_profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialnetwork.R
import com.example.socialnetwork.CircleTransform
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_user_profile.userImage
import java.util.*

class BottomSheet : BottomSheetDialogFragment() {

    lateinit var mListener: OnFragmentInteractionListener

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
        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()

        postButton.setOnClickListener {
            if (postEditText.text.isNotEmpty()) {
                val id = db.collection("posts").document().id
                db.collection("posts").document(id).set(
                    hashMapOf(
                        "user_id" to mAuth.currentUser!!.uid,
                        "content" to postEditText.text.toString(),
                        "date" to Date()
                    )
                )

                dialog?.hide()
                postEditText.text.clear()
                mListener.updatePostList()
            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context должен реализовывать интерфейс OnFragmentInteractionListener"
            )
        }
    }

}