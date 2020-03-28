package com.example.socialnetwork.chat_log

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.ChatLogAdapter
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat_log.*
import kotlinx.android.synthetic.main.main_activity.*

class ChatLogFragment : MvpAppCompatFragment(), ChatLogView {

    @InjectPresenter
    lateinit var presenter: ChatLogPresenter
    lateinit var secondUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.bottom_navigation.visibility = View.GONE

        secondUser = arguments?.getSerializable("user") as User
        presenter.setAdapterAndListener(secondUser)

        sendMessageButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty())
                presenter.sendMessage(messageText, secondUser)

        }

    }

    override fun setAdapter(chatLogAdapter: ChatLogAdapter) {
        chatLogRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = chatLogAdapter
        }
    }

    override fun scrollToPosition(position: Int) {
        chatLogRecyclerView.smoothScrollToPosition(position)
    }

    override fun clearMessageText() {
        messageEditText.text.clear()
    }
}
