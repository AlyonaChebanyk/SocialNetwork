package com.example.socialnetwork.latest_messages

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.socialnetwork.adapters.LatestMessagesAdapter
import com.example.socialnetwork.entities.Message
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

@InjectViewState
class LatestMessagesPresenter: MvpPresenter<LatestMessagesView>() {

    private val dbRealtime = FirebaseDatabase.getInstance()
    private val dbAuth = FirebaseAuth.getInstance()
    private val adapter = LatestMessagesAdapter()

    val latestMessagesHashMap: HashMap<String, Message> = hashMapOf()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setAdapter(adapter)
        setListener()
    }


    private fun setListener(){
        val ref = dbRealtime.getReference("/latest-messages/${dbAuth.currentUser!!.uid}")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)
                latestMessagesHashMap[p0.key!!] = chatMessage!!
                adapter.addMessage(chatMessage)
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)
                latestMessagesHashMap[p0.key!!] = chatMessage!!
                adapter.setMessageList(latestMessagesHashMap.values.toMutableList())
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }
}