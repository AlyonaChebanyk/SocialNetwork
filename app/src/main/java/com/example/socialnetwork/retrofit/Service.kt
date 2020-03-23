package com.example.socialnetwork.retrofit

import com.example.socialnetwork.retrofit.UserResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("api/?inc=name,email,dob,location,phone,login,picture,registered")
    fun addUsers(@Query("results") results: Int): Observable<UserResult>

}