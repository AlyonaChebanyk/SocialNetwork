package com.example.socialnetwork.entities

import com.google.firebase.Timestamp

class Message(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long
){
    constructor(): this("", "", "", "", -1)
}