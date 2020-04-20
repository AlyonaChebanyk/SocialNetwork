package com.example.socialnetwork.chat_log

import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.ChatLogAdapter
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat_log.*
import java.util.*

@InjectViewState
class ChatLogPresenter : MvpPresenter<ChatLogView>() {

    private val dbRealtime = FirebaseDatabase.getInstance()
    private lateinit var adapter: ChatLogAdapter
    val authUser: User = Repository.currentUser!!

    fun setAdapter(secondUser: User) {
        adapter = ChatLogAdapter(secondUser)
        viewState.setAdapter(adapter)
    }

    fun setListener(secondUser: User){
        val messagesRef = dbRealtime.getReference("/messages/${authUser.id}/${secondUser.id}")
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                adapter.addMessage(message!!)
                viewState.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    fun sendMessage(messageText: String, secondUser: User) {

        val fromId = authUser.id
        val toId = secondUser.id

        val reference = dbRealtime.getReference("/messages/$fromId/$toId").push()
//        val chatMessage =
//            Message(reference.key!!, messageText, fromId, toId, System.currentTimeMillis() / 1000)
        val chatMessage =
            Message(reference.key!!, messageText, fromId, toId, Calendar.getInstance().time.time)
        reference.setValue(chatMessage)

        val reference2 = dbRealtime.getReference("/messages/$toId/$fromId").push()
        reference2.setValue(chatMessage)

        val latestMessageRef = dbRealtime.getReference("latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = dbRealtime.getReference("latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

        viewState.clearMessageText()
    }

}
