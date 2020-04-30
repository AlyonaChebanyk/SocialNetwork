package com.example.socialnetwork.retrofit.random_post

import com.google.gson.annotations.SerializedName

data class RandomPost(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String
)