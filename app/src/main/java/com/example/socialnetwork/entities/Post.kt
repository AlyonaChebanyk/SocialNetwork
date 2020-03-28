package com.example.socialnetwork.entities

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Post(
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val commentList: ArrayList<Comment> = arrayListOf()
): Serializable