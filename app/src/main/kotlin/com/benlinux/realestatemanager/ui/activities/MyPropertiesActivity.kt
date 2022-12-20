package com.benlinux.realestatemanager.ui.activities

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
import com.benlinux.realestatemanager.ui.models.User
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.gms.tasks.Task
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
        checkIfUserIsRealtor()?.addOnCompleteListener {
            setToolbar()
            setViews()
            configureViewModel()
            if (userIsRealtor) {
                retrieveRealtorPropertiesData()
            } else {
                retrieveFavoritesData()
            }
            configureRecyclerView()
            showMessageIfNoPropertiesInList()
        }
    }

    private fun checkIfUserIsRealtor(): Task<User?>? {
        return UserManager.getUserData()?.addOnSuccessListener { user ->
           userIsRealtor = user?.isRealtor!!
        }
    }


    private fun redirectUserIfNotLogged() {
        if (!UserManager.isCurrentUserLogged()) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }

    private fun showMessageIfNoPropertiesInList() {
        if (propertiesList.isEmpty()) {
            noPropertyText.visibility = View.VISIBLE
        } else {
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
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define ViewModel
        propertyViewModel = ViewModelProvider(this,
            ViewModelFactory.getInstance(this)!!
        )[PropertyViewModel::class.java]
    }

    private fun retrieveFavoritesData() {
        UserManager.getUserData()?.addOnSuccessListener { user ->
            val favorites = user?.favorites
            Log.d("FAVORITES", favorites.toString())
            if (favorites != null) {
                if (favorites.isEmpty()) {
                    Log.d("FAVORITES", "List of favorites is empty")
                } else {
                    for (favorite in favorites) {
                        val property: Property? =
                            propertyViewModel.getPropertyById(favorite.toInt()).value
                        propertiesList.add(property)
                    }
                }
            }
        }
    }

    private fun retrieveRealtorPropertiesData() {
        UserManager.getUserData()?.addOnSuccessListener { user ->
            val realtorProperties = user?.realtorProperties
            Log.d("REALTOR PROPERTIES", realtorProperties.toString())
            if (realtorProperties != null) {
                if (realtorProperties.isEmpty()) {
                    Log.d("REALTOR PROPERTIES", "List of realtor properties is empty")
                } else {
                    for (realtorProperty in realtorProperties) {
                        val property: Property? = propertyViewModel.getPropertyById(realtorProperty.toInt()).value
                        propertiesList.add(property)
                    }
                }
            }
        }
    }
}