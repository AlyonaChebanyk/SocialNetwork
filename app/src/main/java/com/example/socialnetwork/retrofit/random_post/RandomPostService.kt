package com.example.socialnetwork.retrofit.random_post

import io.reactivex.Observable
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RandomPostService {

    @GET("posts/{id}")
    fun addRandomPost(@Path("id") postId: Int): Observable<RandomPost>

    companion object Factory {
        fun create(): RandomPostService {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()

            return retrofit.create(RandomPostService::class.java)
        }
    }

}