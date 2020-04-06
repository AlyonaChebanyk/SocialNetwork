package com.example.socialnetwork.main_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(){

    private val dbAuth = FirebaseAuth.getInstance()
    private val dbFirestore =FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        dbFirestore.collection("users").document(dbAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                val authUser = User(
                    document.id,
                    document.data!!["full_name"] as String,
                    document.data!!["user_name"] as String,
                    document.data!!["picture"] as String
                )

                val bundle = bundleOf("authUser" to authUser)
                findNavController(R.id.nav_host_fragment).navigate(R.id.userProfileFragment, bundle)

                bottom_navigation.setOnNavigationItemReselectedListener { item ->
                    when (item.itemId) {
                        R.id.nav_home -> findNavController(
                            R.id.nav_host_fragment
                        ).navigate(R.id.homeFragment, bundle)
                        R.id.nav_profile -> findNavController(
                            R.id.nav_host_fragment
                        ).navigate(R.id.userProfileFragment, bundle)
                        R.id.nav_search -> findNavController(
                            R.id.nav_host_fragment
                        ).navigate(R.id.searchFragment, bundle)
                        R.id.nav_messaging -> findNavController(
                            R.id.nav_host_fragment
                        ).navigate(R.id.latestMessagesFragment, bundle)
                    }
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
                dbAuth.signOut()
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.authorisationActivity
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
