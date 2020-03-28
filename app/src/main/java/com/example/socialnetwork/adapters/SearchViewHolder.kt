package com.example.socialnetwork.adapters

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.for_round_image.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.person_item.view.*

class SearchViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(user: User){
        with(view){
            Picasso.get()
                .load(user.picture)
                .transform(CircleTransform())
                .resize(90, 90)
                .into(userImage)

            userFullNameTextView.text = user.fullName
            userNameTextView.text = "@" + user.userName

            person_item.setOnClickListener {
                val bundle = bundleOf("user" to user)
                findNavController().navigate(R.id.action_searchFragment_to_userPageFragment, bundle)
            }
        }
    }

}