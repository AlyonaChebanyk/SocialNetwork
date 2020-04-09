package com.example.socialnetwork.post_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.socialnetwork.R
import com.example.socialnetwork.adapters.CommentAdapter
import com.example.socialnetwork.entities.Post
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_post_page.*
import kotlinx.android.synthetic.main.fragment_user_page.userLoginTextView
import kotlinx.android.synthetic.main.activity_main.*

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

        val user = arguments!!.getParcelable<User>("user")!!
        val post = arguments!!.getParcelable<Post>("post")!!

        presenter.setAdapter(user)
        presenter.setListener(user, post)

        addCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotEmpty()){
                presenter.addComment(commentText, user, post)
            }

        }

    }

    override fun displayPostData(user: User, post: Post) {
        Picasso.get()
            .load(user.picture)
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
