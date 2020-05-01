package com.example.socialnetwork.main_activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity() {

    private val dbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bottom_navigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_out, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                dbAuth.signOut()
                Repository.currentUser = null

                val pref = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean("isSingedIn", false)
                editor.apply()

                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.singInFragment
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
