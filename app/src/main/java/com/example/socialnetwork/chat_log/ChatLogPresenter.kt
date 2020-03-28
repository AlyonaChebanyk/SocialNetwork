package com.example.socialnetwork.chat_log

import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.ChatLogAdapter
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat_log.*

@InjectViewState
class ChatLogPresenter : MvpPresenter<ChatLogView>() {

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbRealtime = FirebaseDatabase.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: User
    lateinit var adapter: ChatLogAdapter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()


    }

    fun setAdapterAndListener(secondUser: User){
        dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                currentUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String,
                    document.data!!["following"] as ArrayList<String>
                )

                adapter = ChatLogAdapter(secondUser)
                viewState.setAdapter(adapter)

                val messagesRef = dbRealtime.getReference("/messages/${currentUser.id}/${secondUser.id}")
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
    }

    fun sendMessage(messageText: String, secondUser: User) {

        val fromId = currentUser.id
        val toId = secondUser.id

        val reference = dbRealtime.getReference("/messages/$fromId/$toId").push()
        val chatMessage = Message(reference.key!!, messageText, fromId, toId, System.currentTimeMillis() / 1000)
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