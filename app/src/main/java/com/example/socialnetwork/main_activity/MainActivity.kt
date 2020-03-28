package com.example.socialnetwork.main_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.socialnetwork.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : MvpAppCompatActivity(){

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)

        bottom_navigation.selectedItemId =
            R.id.nav_profile
        bottom_navigation.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> findNavController(
                    R.id.nav_host_fragment
                ).navigate(R.id.homeFragment)
                R.id.nav_profile -> findNavController(
                    R.id.nav_host_fragment
                ).navigate(R.id.userProfileFragment)
                R.id.nav_search -> findNavController(
                    R.id.nav_host_fragment
                ).navigate(R.id.searchFragment)
                R.id.nav_messaging -> findNavController(
                    R.id.nav_host_fragment
                ).navigate(R.id.latestMessagesFragment)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_out, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                mAuth.signOut()
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.singInFragment
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
