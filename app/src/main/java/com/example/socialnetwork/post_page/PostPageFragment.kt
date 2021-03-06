package com.example.socialnetwork.post_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.DateToStringConverter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.example.socialnetwork.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_page.*
import kotlinx.android.synthetic.main.fragment_post_page.userNameTextView
import java.util.*

class PostPageFragment : MvpAppCompatFragment(), PostPageView {

    @InjectPresenter
    lateinit var presenter: PostPagePresenter

    lateinit var user: User
    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().bottom_navigation.visibility = View.GONE
        requireActivity().toolbar.visibility = View.GONE

        user = requireArguments().getParcelable("user")!!
        post = requireArguments().getParcelable("post")!!

        presenter.setListener(user, post)
        presenter.displayPostData(user, post)

        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotEmpty()){
                presenter.addComment(commentText, user, post)
            }

        }

        userImagePostPage.setOnClickListener {
            if (user.id == Repository.currentUser!!.id)
                findNavController().navigate(R.id.action_postPage_to_userProfileFragment)
            else{
                val bundle = bundleOf("user" to user)
                findNavController().navigate(R.id.action_postPage_to_userPageFragment, bundle)
            }
        }

    }

    override fun displayPostData(user: User, post: Post) {
        Picasso.get()
            .load(user.picture)
            .into(userImagePostPage)

        userNameTextView.text = user.fullName
        userLoginTextView.text = "@" + user.userName

        postTextTextView.text = post.content
        postDateTextView.text = DateToStringConverter.getDaysAgo(Date(post.timestamp))
        if(post.postImageUrl != ""){
            Picasso.get()
                .load(post.postImageUrl)
                .into(postImage)
        }
    }

    override fun setAdapter(commentAdapter: CommentAdapter) {
        commentListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = commentAdapter
        }
    }

    override fun clearCommentText() {
        commentEditText.text.clear()
    }
}
