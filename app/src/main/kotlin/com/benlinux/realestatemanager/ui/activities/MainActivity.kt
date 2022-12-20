package com.benlinux.realestatemanager.ui.activities

 import android.annotation.SuppressLint
 import android.content.Intent
 import android.os.Bundle
 import android.util.Log
 import android.view.MenuItem
 import android.view.View
 import android.widget.ImageView
 import android.widget.TextView
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
 import com.benlinux.realestatemanager.R
 import com.benlinux.realestatemanager.data.userManager.UserManager
 import com.benlinux.realestatemanager.ui.fragments.MapFragment
 import com.benlinux.realestatemanager.utils.isInternetAvailable
 import com.bumptech.glide.Glide
 import com.bumptech.glide.request.RequestOptions
 import com.google.android.gms.maps.model.LatLng
 import com.google.android.material.bottomnavigation.BottomNavigationView
 import com.google.android.material.floatingactionbutton.FloatingActionButton
 import com.google.android.material.navigation.NavigationView
 import java.util.*


open class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerNavView: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mapFragment: MapFragment

    private lateinit var addPropertyButton: FloatingActionButton

    var realtorLocation: LatLng? = null

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userAvatar: ImageView

    private var userIsRealtor = false


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
        setUserDataInDrawer()
        setUserOptionsInDrawer()

    }

    private fun isUserConnected(): Boolean {
        return UserManager.isCurrentUserLogged()
    }

    private fun isUserRealtor(): Boolean {
        return userIsRealtor
    }

    private fun setUserDataInDrawer() {
        val headerContainer = drawerNavView.getHeaderView(0)
        userName = headerContainer.findViewById(R.id.user_name)
        userEmail = headerContainer.findViewById(R.id.user_email)
        userAvatar = headerContainer.findViewById(R.id.user_avatar)

        UserManager.getUserData()?.addOnSuccessListener { user ->
            if (user != null) {
                // Set user name
                userName.text = buildString {
                    append(user.firstName)
                    append(" ")
                    append(user.lastName)
                }
                Log.d("USER NAME",
                    buildString { append(user.firstName)
                    append(" ")
                    append(user.lastName) })

                // Set user email
                userEmail.text = user.email
                // Set avatar
                if (user.avatarUrl.isEmpty()) {
                    Glide.with(this)
                        .load(R.mipmap.no_photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(userAvatar)
                } else {
                    Glide.with(this)
                        .load(user.avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(userAvatar)
                }
                userIsRealtor = user.isRealtor

            }
            // Update menu options in drawer according to user status
            setUserOptionsInDrawer()

        }
        if (!isUserConnected()) {
            userName.text = getString(R.string.info_no_username_found)
            userEmail.text = getString(R.string.please_login)

            Glide.with(this)
                .load(R.mipmap.no_photo)
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar)
        }
        Log.d("DRAWER :", "user data updated" )
    }

    private fun setUserOptionsInDrawer(){
        val loginItem: MenuItem = drawerNavView.menu.findItem(R.id.drawer_navigation_login)
        loginItem.isVisible = !isUserConnected()

        val myPropertiesItem = drawerNavView.menu.findItem(R.id.drawer_navigation_my_properties)
        myPropertiesItem.isVisible = (isUserConnected() && isUserRealtor())

        val myFavoritesItem = drawerNavView.menu.findItem(R.id.drawer_navigation_my_favorites)
        myFavoritesItem.isVisible = (isUserConnected() && !isUserRealtor())

        val settingsItem = drawerNavView.menu.findItem(R.id.drawer_navigation_settings)
        settingsItem.isVisible = isUserConnected()

        val logoutItem = drawerNavView.menu.findItem(R.id.drawer_navigation_logout)
        logoutItem.isVisible = isUserConnected()

        Log.d("DRAWER :", "menu options updated" )

    }

    // Set fragments views (map)
    private fun setFragments() {
        mapFragment = MapFragment()
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
            R.id.drawer_navigation_my_favorites -> {
                val favoritesActivityIntent = Intent(this, MyPropertiesActivity::class.java)
                startActivity(favoritesActivityIntent)
            }
            R.id.drawer_navigation_my_properties -> {
                val myPropertiesActivityIntent = Intent(this, MyPropertiesActivity::class.java)
                startActivity(myPropertiesActivityIntent)
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
        UserManager.signOut(this)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.disconnection_succeed), Toast.LENGTH_SHORT).show()
                Log.d("LOGOUT :", "SUCCESS" )
                setUserDataInDrawer()
                setUserOptionsInDrawer()
            }
            .addOnFailureListener {
                // On failure, show error toast
                Toast.makeText(this, getString(R.string.disconnection_failed), Toast.LENGTH_SHORT).show()
                Log.d("LOGOUT :", "FAILURE" )
            }
    }

    fun showAddButton() {
        addPropertyButton.visibility = View.VISIBLE
    }

    fun hideAddButton() {
        addPropertyButton.visibility = View.GONE
    }


}