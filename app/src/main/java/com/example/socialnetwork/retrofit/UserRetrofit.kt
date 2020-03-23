package com.example.socialnetwork.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class UserRetrofit(
    @SerializedName("name")
    val name: Name,
    @SerializedName("email")
    val email: String,
    @SerializedName("dob")
    val dob: Dob,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("login")
    val login: Login,
    @SerializedName("picture")
    val picture: Picture,
    @SerializedName("registered")
    val registered: Registered
): Serializable

data class Picture(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
): Serializable

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
): Serializable

data class Dob(
    @SerializedName("date")
    val date: Date
): Serializable

data class Login(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
): Serializable

data class Registered(
    @SerializedName("date")
    val date: Date
): Serializable

data class UserResult(
    @SerializedName("results")
    val userRetrofitList: ArrayList<UserRetrofit>
)

