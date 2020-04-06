package com.example.socialnetwork.user_profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.for_round_image.CircleTransform
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_user_page.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.collections.ArrayList

class AddPostBottomSheet : BottomSheetDialogFragment() {

    private val RESULT_LOAD_IMG = 1
    private val dbStorage = FirebaseStorage.getInstance()
    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbRealtime = FirebaseDatabase.getInstance()
    var postImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments!!.getParcelable<User>("user")!!

        postButton.setOnClickListener {
            if (postEditText.text.isNotEmpty()) {

                val reference = dbRealtime.getReference("/user-posts/${user.id}").push()

                if (postImageUri!=null){
                    val imageStream: InputStream? =
                        activity!!.contentResolver.openInputStream(postImageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)

                    val storageRef = dbStorage.reference
                    val imageRef: StorageReference = storageRef.child(reference.key!!)
                    val baos = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data1 = baos.toByteArray()

                    imageRef.putBytes(data1).addOnCompleteListener {
                        imageRef.downloadUrl.addOnSuccessListener { it2 ->
                            val postImageUrl = it2.toString()

                            val userPost = Post(reference.key!!, user.id, postEditText.text.toString(), postImageUrl = postImageUrl)
                            reference.setValue(userPost)

                            postEditText.text.clear()

                            dbFirestore.collection("users").get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        val following = document.data["following"] as ArrayList<String>
                                        if (following.contains(user.id)) {
                                            val userId = document.id
                                            val ref = dbRealtime.getReference("/following-posts/$userId").push()
                                            ref.setValue(userPost)
                                        }
                                    }
                                }
                        }

                    }

                }else{
                    val userPost = Post(reference.key!!, user.id, postEditText.text.toString())
                    reference.setValue(userPost)

                    dbFirestore.collection("users").get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val following = document.data["following"] as ArrayList<String>
                                if (following.contains(user.id)) {
                                    val userId = document.id
                                    val ref = dbRealtime.getReference("/following-posts/$userId").push()
                                    ref.setValue(userPost)
                                }
                            }
                        }
                    postEditText.text.clear()
                }


                dialog?.hide()
            }

        }

        addImageToPostButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                postImageUri = data?.data

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(activity, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}