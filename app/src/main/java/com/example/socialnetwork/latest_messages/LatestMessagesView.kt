package com.example.socialnetwork.latest_messages

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.LatestMessagesAdapter

interface LatestMessagesView: MvpView {

    fun setAdapter(latestMessagesAdapter: LatestMessagesAdapter)

}