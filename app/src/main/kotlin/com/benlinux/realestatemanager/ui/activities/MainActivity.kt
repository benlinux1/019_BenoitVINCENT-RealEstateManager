package com.benlinux.realestatemanager.ui.activities

 import android.annotation.SuppressLint
 import android.content.Intent
 import android.os.Bundle
 import android.view.MenuItem
 import android.widget.Toast
 import androidx.appcompat.app.ActionBarDrawerToggle
 import androidx.appcompat.app.AppCompatActivity
 import androidx.appcompat.widget.Toolbar
 import androidx.core.view.GravityCompat
 import androidx.drawerlayout.widget.DrawerLayout
 import androidx.navigation.NavController
 import androidx.navigation.fragment.NavHostFragment
 import androidx.navigation.ui.AppBarConfiguration
 import androidx.navigation.ui.NavigationUI.navigateUp
 import androidx.navigation.ui.NavigationUI.setupWithNavController
 import androidx.navigation.ui.onNavDestinationSelected
 import androidx.navigation.ui.setupActionBarWithNavController
 import androidx.recyclerview.widget.RecyclerView
 import com.benlinux.realestatemanager.R
 import com.benlinux.realestatemanager.ui.adapters.ListAdapter
 import com.benlinux.realestatemanager.ui.fragments.AddPropertyFragment
 import com.benlinux.realestatemanager.ui.fragments.MapFragment
 import com.benlinux.realestatemanager.ui.models.Property
 import com.benlinux.realestatemanager.utils.isInternetAvailable
 import com.google.android.material.bottomnavigation.BottomNavigationView
 import com.google.android.material.floatingactionbutton.FloatingActionButton
 import com.google.android.material.navigation.NavigationView
 import java.util.*


class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerNavView: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mapFragment: MapFragment
    private lateinit var addPropertyFragment: AddPropertyFragment

    private lateinit var addPropertyButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragments()
        configureToolBar()
        checkInternetConnection()
        setUpDrawerNavigation()
        setUpBottomNavigation()
        configureDrawerLayoutToggle()
        setAddButton()

    }

    // Set fragments views (map, add property)
    private fun setFragments() {
        mapFragment = MapFragment()
        addPropertyFragment = AddPropertyFragment()
    }

    // Set Add Property Floating action Button
    private fun setAddButton() {
        addPropertyButton = findViewById(R.id.add_property_button)
        addPropertyButton.setOnClickListener {
            val addPropertyActivityIntent = Intent(this, AddPropertyActivity::class.java)
            startActivity(addPropertyActivityIntent)
            finish()
        }
    }

    private fun checkInternetConnection() {
        isInternetAvailable(applicationContext)
    }

    // Setup custom Toolbar
    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation  = 16f
    }


    // Configure Drawer Navigation
    private fun setUpDrawerNavigation() {

        // Set navigation controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set drawer navigation views
        drawerNavView = findViewById(R.id.activity_main_nav_view)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)

        // Build and configure App bar
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph) //Pass the ids of fragments from nav_graph which you don't want to show back button in toolbar
            .setOpenableLayout(drawerLayout)
            .build()

        // Setup action bar navigation for drawer
        setupActionBarWithNavController(navController, drawerLayout)

        // Setup Drawer Navigation with its items
        drawerNavView.setNavigationItemSelectedListener(this)
    }


    // Configure Drawer Layout with toggle
    private fun configureDrawerLayoutToggle() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setUpBottomNavigation() {
        // Setup bottom navigation (action bar title & back button)
        bottomNavView = findViewById(R.id.bottom_nav_view)
        setupWithNavController(bottomNavView, navController)
    }

    // Handle Navigation Item Click
    @SuppressLint("NonConstantResourceId")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_navigation_login -> {
                val loginActivityIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginActivityIntent)
            }
            R.id.drawer_navigation_settings -> {
                val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivityIntent)
            }
            R.id.drawer_navigation_logout -> {
                logout()
            }

            else -> item.onNavDestinationSelected(navController)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Handles hamburger and back button click
    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, appBarConfiguration)
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