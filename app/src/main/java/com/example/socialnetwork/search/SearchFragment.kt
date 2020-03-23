package com.example.socialnetwork.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.socialnetwork.R
import com.example.socialnetwork.CircleTransform
import com.example.socialnetwork.adapters.SearchAdapter
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.main_activity.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE

        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val searchList = arrayListOf<User>()
        val searchAdapter = SearchAdapter()

        val authUserId = mAuth.currentUser!!.uid
        var authUserImage = ""
        db.collection("users").document(authUserId).get().addOnSuccessListener { document ->
            authUserImage = document.data!!["picture"] as String
            Picasso.get()
                .load(authUserImage)
                .transform(CircleTransform())
                .resize(110, 110)
                .into(userImage)
        }

        searchListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }

        searchEditText.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchList.clear()
                db.collection("users").get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents){
                            val fullName = doc.data["full_name"] as String
                            val userName = doc.data["user_name"] as String
                            if(fullName.contains(text.toString()) || userName.contains(text.toString()))
                                searchList.add(User(doc.id, fullName,
                                    userName,
                                    doc.data["picture"] as String,
                                    doc.data["following"] as MutableList<String>))
                        }
                        searchAdapter.setList(searchList)
                    }

            }
        })
    }

}
