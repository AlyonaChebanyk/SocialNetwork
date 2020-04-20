package com.example.socialnetwork.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Post(
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val postImageUrl: String = "",
    val timestamp: Long = -1
) : Parcelable