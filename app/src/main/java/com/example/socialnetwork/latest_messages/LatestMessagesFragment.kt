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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_latest_messages.*

class LatestMessagesFragment : MvpAppCompatFragment(), LatestMessagesView {

    @InjectPresenter
    lateinit var presenter: LatestMessagesPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
    }

    override fun setAdapter(latestMessagesAdapter: LatestMessagesAdapter) {
        latestMessagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = latestMessagesAdapter
        }
    }
}

