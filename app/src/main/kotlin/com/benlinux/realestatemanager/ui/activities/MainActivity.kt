package com.benlinux.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.utils.isInternetAvailable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerNavView: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
        checkInternetConnection()
        configureNavigation()
        configureDrawerLayout()

    }

    private fun setViews() {

    }

    private fun checkInternetConnection() {
        isInternetAvailable(applicationContext)
    }


    override fun onBackPressed() {
        // Handle back click to close menu
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Replace navigation up button with nav drawer button when on start destination
        return navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Configure Drawer & Bottom Navigation
    private fun configureNavigation() {

        // Set navigation views
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        drawerNavView = findViewById<NavigationView>(R.id.activity_main_nav_view)
        drawerLayout = findViewById<DrawerLayout>(R.id.activity_main_drawer_layout)

        // Set navigation controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Build and configure App bar
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph) //Pass the ids of fragments from nav_graph which you don't want to show back button in toolbar
                .setOpenableLayout(drawerLayout)
                .build()

        //Setup toolbar with back button and drawer icon according to appBarConfiguration
        setupActionBarWithNavController(this, navController, appBarConfiguration)

        // Setup Navigation for drawer & bottom bar
        setupWithNavController(bottomNavView, navController)
        setupWithNavController(drawerNavView, navController)

        // Listener for selected item
        drawerNavView.setNavigationItemSelectedListener(this)
    }

    // Configure Drawer Layout with toggle
    private fun configureDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    // Handle Navigation Item Click
    @SuppressLint("NonConstantResourceId")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_navigation_login -> {
                val lunchActivityIntent = Intent(this, LoginActivity::class.java)
                startActivity(lunchActivityIntent)
            }
            R.id.drawer_navigation_settings -> {
                val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivityIntent)
            }
            R.id.drawer_navigation_logout -> logout()
            else -> {}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Logout from firebase
    private fun logout() {
        // On success, close activity & go to login
        Toast.makeText(this, getString(R.string.disconnection_succeed), Toast.LENGTH_SHORT)
            .show()
        // On failure, show error toast
        Toast.makeText(this, getString(R.string.disconnection_failed), Toast.LENGTH_SHORT)
            .show()
    }

}