package com.example.socialnetwork.latest_messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.LatestMessagesAdapter
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_latest_messages.*

class LatestMessagesFragment : MvpAppCompatFragment(), LatestMessagesView {

    @InjectPresenter
    lateinit var presenter: LatestMessagesPresenter

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    presenter.setAdapter(authUser!!)
                    presenter.setListener(authUser!!)
                }

        } else {
            presenter.setAdapter(authUser!!)
            presenter.setListener(authUser!!)
        }

    }

    override fun setAdapter(latestMessagesAdapter: LatestMessagesAdapter) {
        latestMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = latestMessagesAdapter
        }
    }
}

