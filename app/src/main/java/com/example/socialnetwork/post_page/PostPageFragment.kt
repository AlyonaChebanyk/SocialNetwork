package com.example.socialnetwork.post_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.for_round_image.CircleTransform
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_post_page.*
import kotlinx.android.synthetic.main.fragment_user_page.userLoginTextView
import kotlinx.android.synthetic.main.main_activity.*

class PostPageFragment : MvpAppCompatFragment(), PostPageView {

    @InjectPresenter
    lateinit var presenter: PostPagePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.bottom_navigation.visibility = View.GONE

        val user = arguments!!.getSerializable("user") as User
        val post = arguments!!.getSerializable("post") as Post

        presenter.setUser_(user)
        presenter.setPost_(post)

        presenter.setListener()

        addCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotEmpty()){
                presenter.addComment(commentText)
            }

        }

    }

    override fun displayPostData(user: User, post: Post) {
        Picasso.get()
            .load(user.picture)
            .resize(150, 150)
            .transform(CircleTransform())
            .into(userImagePostPage)

        userNameTextView.text = user.fullName
        userLoginTextView.text = user.userName

        postTextTextView.text = post.content
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
