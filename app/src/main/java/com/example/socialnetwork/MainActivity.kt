package com.example.socialnetwork

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.socialnetwork.user_profile.OnFragmentInteractionListener
import com.example.socialnetwork.user_profile.UserProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : MvpAppCompatActivity(), OnFragmentInteractionListener {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        bottom_navigation.selectedItemId = R.id.nav_profile
        bottom_navigation.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                R.id.nav_profile -> findNavController(R.id.nav_host_fragment).navigate(R.id.userProfileFragment)
                R.id.nav_search -> findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment)
            }
        }
    }

    override fun updatePostList() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val fragment = navHostFragment!!.childFragmentManager.fragments[0] as UserProfileFragment
        fragment.updatePostList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_out, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                mAuth.signOut()
                findNavController(R.id.nav_host_fragment).navigate(R.id.singInFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
