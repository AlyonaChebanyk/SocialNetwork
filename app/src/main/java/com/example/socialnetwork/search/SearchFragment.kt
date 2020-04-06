package com.example.socialnetwork.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import com.example.socialnetwork.R
import com.example.socialnetwork.for_round_image.CircleTransform
import com.example.socialnetwork.adapters.SearchAdapter
import com.example.socialnetwork.entities.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.activity_main.*

class SearchFragment : MvpAppCompatFragment(), SearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.toolbar.visibility = View.GONE

        val authUser = arguments!!.getParcelable<User>("authUser")!!

        presenter.setAdapter(authUser)

        searchEditText.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updateSearchList(text.toString())
            }
        })
    }

    override fun setAdapter(searchAdapter: SearchAdapter) {
        searchListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }
    }
}
