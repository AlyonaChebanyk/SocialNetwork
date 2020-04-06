package com.example.socialnetwork.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val id: String,
    val fullName: String,
    val userName: String,
    val picture: String
): Parcelable