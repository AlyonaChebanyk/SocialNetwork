package com.example.socialnetwork.adapters

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.person_item.view.*

class SearchViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(user: User, authUser: User) {
        with(view) {
            Picasso.get()
                .load(user.picture)
                .into(userImage)

            userFullNameTextView.text = user.fullName
            userNameTextView.text = "@" + user.userName

            person_item.setOnClickListener {
                val bundle = bundleOf(
                    "authUser" to authUser,
                    "user" to user
                )
                findNavController().navigate(R.id.action_searchFragment_to_userPageFragment, bundle)
            }
        }
    }

}