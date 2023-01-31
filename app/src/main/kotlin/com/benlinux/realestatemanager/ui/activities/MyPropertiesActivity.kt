package com.benlinux.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration


class MyPropertiesActivity: AppCompatActivity() {

    private lateinit var noPropertyText: TextView
    private var propertiesList: MutableList<Property?> = mutableListOf()
    private lateinit var propertyRecyclerView: RecyclerView
    private lateinit var propertyAdapter: ListAdapter

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    private var userIsRealtor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        redirectUserIfNotLogged()
        checkIfUserIsRealtor()
        setToolbar()
        setViews()
        configureRecyclerView()
        configureViewModel()
        if (userIsRealtor) {
            retrieveRealtorPropertiesData()
        } else {
            retrieveFavoritesData()
        }

    }

    private fun checkIfUserIsRealtor() {
        val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        userIsRealtor = sharedPreferences.getBoolean("realtor", true)
        Log.d("REALTOR STATUS", userIsRealtor.toString())
    }


    private fun redirectUserIfNotLogged() {
        if (!UserManager.isCurrentUserLogged()) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }

    // Show message if list is empty, according to user status (realtor / user)
    private fun showMessageIfNoPropertiesInList() {
        if (propertiesList.isEmpty()) {
            // Display "No property found"
            noPropertyText.visibility = View.VISIBLE
            if (userIsRealtor)  {
                noPropertyText.text = getString(R.string.no_property_found_realtor)
            } else {
                noPropertyText.text = getString(R.string.no_property_found_user)
            }
        } else {
            // Hide "No property found"
            noPropertyText.visibility = View.GONE
        }
    }

    private fun setToolbar() {
        val mToolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        if (userIsRealtor) {
            supportActionBar?.title = resources.getString(R.string.realtor_properties_toolbar_title)
        } else {
            supportActionBar?.title = resources.getString(R.string.user_favorites_toolbar_title)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViews() {
        noPropertyText = findViewById(R.id.text_no_properties)
        propertyRecyclerView = findViewById(R.id.list_my_properties)
    }

    private fun configureRecyclerView() {
        propertyAdapter = ListAdapter(propertiesList, this)
        propertyRecyclerView.layoutManager = LinearLayoutManager(this)
        val divider = MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        propertyRecyclerView.addItemDecoration(divider)
        propertyRecyclerView.adapter = propertyAdapter
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define ViewModel
        propertyViewModel = ViewModelProvider(this,
            ViewModelFactory.getInstance(this)!!
        )[PropertyViewModel::class.java]

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveFavoritesData() {
        UserManager.getUserData()?.addOnSuccessListener { user ->
            val favorites = user?.favorites
            Log.d("FAVORITES", favorites.toString())
            if (favorites != null) {
                if (favorites.isEmpty()) {
                    Log.d("FAVORITES", "List of favorites is empty")
                    showMessageIfNoPropertiesInList()
                } else {
                    for (favorite in favorites) {
                        propertyViewModel.getPropertyById(favorite!!.toInt()).observe(this) {
                            val property = it
                            Log.d("FAVORITE", property.toString())
                            propertiesList.add(property)
                            propertyAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveRealtorPropertiesData() {
        UserManager.getUserData()?.addOnSuccessListener { user ->
            val realtorProperties = user?.realtorProperties
            Log.d("REALTOR PROPERTIES", realtorProperties.toString())
            if (realtorProperties != null) {
                if (realtorProperties.isEmpty()) {
                    Log.d("REALTOR PROPERTIES", "List of realtor properties is empty")
                    showMessageIfNoPropertiesInList()
                } else {
                    for (realtorProperty in realtorProperties) {
                        propertyViewModel.getPropertyById(realtorProperty!!.toInt()).observe(this) {
                            val property = it
                            if (it != null) {
                                Log.d("REALTOR PROPERTY", it.toString())
                                propertiesList.add(property)
                                propertyAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }
}