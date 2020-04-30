package com.example.socialnetwork.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.retrofit.random_post.RandomPostService
import com.example.socialnetwork.retrofit.random_user.RandomUserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

object Repository {
    var currentUser: User? = null

    private val dbAuth = FirebaseAuth.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbRealtime = FirebaseDatabase.getInstance()
    private val dbStorage = FirebaseStorage.getInstance()
    private val service = RandomPostService.create()
    lateinit var postUserId: String

    @SuppressLint("CheckResult")
    fun uploadRandomPost(context: Context, randomUser: Boolean = false) {

        fun uploadRandomPostToUser(userId: String){ val getImage = (0..2).random()

            val reference = dbRealtime.getReference("/user-posts/${userId}").push()

            if (getImage == 0) {

                val storageRef = dbStorage.reference
                val imageRef: StorageReference = storageRef.child(reference.key!!)

                val randImageId = (1..45).random()

                val uri: Uri = Uri.parse("android.resource://"+ context.packageName +"/drawable/picture$randImageId")
                imageRef.putFile(uri).addOnSuccessListener {

                    imageRef.downloadUrl.addOnSuccessListener { it2 ->
                        val postImageUrl = it2.toString()

                        service.addRandomPost((1..100).random())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { randomPost ->
                                val userPost = Post(
                                    reference.key!!,
                                    userId,
                                    randomPost.body,
                                    postImageUrl,
                                    timestamp = Calendar.getInstance().timeInMillis
                                )
                                reference.setValue(userPost)

                                dbFirestore.collection("users").get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val following =
                                                document.data["following"] as ArrayList<String>
                                            if (following.contains(userId)) {
                                                val currentUserId = document.id
                                                val ref =
                                                    dbRealtime.getReference("/following-posts/$currentUserId")
                                                        .push()
                                                ref.setValue(userPost)
                                            }
                                        }
                                    }
                            }


                    }

                }

            } else {

                service.addRandomPost((1..100).random())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {randomPost ->
                        val userPost = Post(
                            reference.key!!,
                            userId,
                            randomPost.body,
                            timestamp = Calendar.getInstance().timeInMillis
                        )
                        reference.setValue(userPost)

                        dbFirestore.collection("users").get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val following = document.data["following"] as ArrayList<String>
                                    if (following.contains(userId)) {
                                        val currentUserId = document.id
                                        val ref =
                                            dbRealtime.getReference("/following-posts/$currentUserId").push()
                                        ref.setValue(userPost)
                                    }
                                }
                            }
                    }

            }}

        if (!randomUser){
            postUserId = currentUser!!.id
            uploadRandomPostToUser(postUserId)
        }else{
            val userIdList = arrayListOf<String>()

            dbFirestore.collection("users").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        userIdList.add(document.id)
                    }
                    postUserId = userIdList[Random().nextInt(userIdList.size)]
                    uploadRandomPostToUser(postUserId)
                }
        }

    }

    @SuppressLint("CheckResult")
    fun uploadUsers(amount: Int = 10) {
        Timber.plant(Timber.DebugTree())

        val service = RandomUserService.create()

        service.addUsers(amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                for (user in result.userRetrofitList) {
                    dbAuth.createUserWithEmailAndPassword(user.email, user.login.password)
                        .addOnSuccessListener {
                            Timber.d("${user.email}:  ${user.login.password}")
                            dbAuth.signInWithEmailAndPassword(user.email, user.login.password)
                            val userId = dbAuth.currentUser!!.uid

                            dbFirestore.collection("users").document(userId)
                                .set(
                                    hashMapOf(
                                        "full_name" to "${user.name.first} ${user.name.last}",
                                        "user_name" to user.login.username,
                                        "picture" to user.picture.medium,
                                        "following" to arrayListOf<String>()
                                    )
                                )
                        }

                }

            }
    }
}