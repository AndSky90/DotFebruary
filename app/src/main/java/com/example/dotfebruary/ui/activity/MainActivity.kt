package com.example.dotfebruary.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.dotfebruary.R
import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


//google api project com-example-dotfebruary
//quakesky
//facebook id 508803756709055

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)

        //private lateinit var appBarConfiguration: AppBarConfiguration

        //  NavigationUI.setupWithNavController(toolbar, navController, drawerLayout)
        //  navView.setupWithNavController(navController)
        //  NavigationUI.setupWithNavController(navView, navController)
        //  NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        /*  navView.avatar
          navView.name.text =
          navView.email*/

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                navView.bringToFront()
                drawerLayout.requestLayout()
                super.onDrawerOpened(drawerView)
            }
        }
        navView.setNavigationItemSelectedListener { p0 ->
            when (p0.itemId) {
                R.id.action_logout -> {
                    LoginManager.getInstance().logOut()
                    startSplash()
                }
                R.id.action_github_search -> navController.navigate(R.id.githubSearchFragment)
                else -> {
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun startSplash() {
        startActivity(Intent(this, SplashActivity::class.java))
        this.finish()
    }

}
