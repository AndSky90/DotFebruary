package com.example.dotfebruary.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.dotfebruary.R
import com.example.dotfebruary.common.setAvatarWithPicasso
import com.example.dotfebruary.ui.activity.splash.SplashActivity
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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
                else -> {
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val navHeader: View = navView.getHeaderView(0)

        viewModel.getFacebookProfileFromDb().observe(this, Observer {
            setAvatarWithPicasso(navHeader.avatar, getFacebookAvatarUrl(it.id) )
            navHeader.name.text = it.name
            navHeader.email.text = it.email
        })
    }

    private fun getFacebookAvatarUrl(profileId: String): String =
        "https://graph.facebook.com/$profileId/picture?width=500&height=500"

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
