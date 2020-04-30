package com.example.socialnetwork.retrofit.random_user

import io.reactivex.Observable
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserService {

    @GET("api/?inc=name,email,dob,location,phone,login,picture,registered")
    fun addUsers(@Query("results") results: Int): Observable<UserResult>

    companion object Factory {
        fun create(): RandomUserService {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://randomuser.me/")
                .build()

            return retrofit.create(RandomUserService::class.java)
        }
    }

}