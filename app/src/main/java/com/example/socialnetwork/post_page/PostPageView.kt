package com.example.socialnetwork.post_page

import com.arellomobile.mvp.MvpView
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User

interface PostPageView: MvpView {

    fun displayPostData(user: User, post: Post)
    fun setAdapter(commentAdapter: CommentAdapter)
    fun clearCommentText()

}