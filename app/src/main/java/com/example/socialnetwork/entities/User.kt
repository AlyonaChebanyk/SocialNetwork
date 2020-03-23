package com.example.socialnetwork.entities

import java.io.Serializable

class User(
    val id: String,
    val fullName: String,
    val userName: String,
    val picture: String,
    val following: MutableList<String>
): Serializable