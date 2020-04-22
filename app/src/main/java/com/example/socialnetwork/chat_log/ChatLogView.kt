package com.example.socialnetwork.chat_log

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.ChatLogAdapter
import com.example.socialnetwork.entities.User

interface ChatLogView: MvpView {

    fun setAdapter(chatLogAdapter: ChatLogAdapter)
    fun scrollToPosition(position: Int)
    fun clearMessageText()
    fun setListenerToSendMessageButton()
    fun displaySecondUserName()
    fun setListenerToGotoLatestMessagesButton()

}