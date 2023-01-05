package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.propertyManager.PropertyManager
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.convertStringToDate
import com.benlinux.realestatemanager.utils.isInternetAvailable
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.util.*

class ListFragment: Fragment() {

    // The fragment root view
    private lateinit var fragmentView: View

    // The recycler view that contains properties
    private lateinit var mRecyclerView: RecyclerView

    // List of all current properties in the application
    private var mProperties: MutableList<Property?> = mutableListOf()

    // The adapter which handles the list of properties
    private lateinit var adapter: ListAdapter

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // TextViews
    private lateinit var mLabelNoProperty: TextView
    private lateinit var mFragmentTitle: TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_list_view, container, false)
        configureViewModel()
        configureRecyclerView()

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {

        // Define ViewModel
         propertyViewModel = ViewModelProvider(this,
             ViewModelFactory.getInstance(requireContext())!!
         )[PropertyViewModel::class.java]

        // Set observer on properties list
        propertyViewModel.currentProperties?.observe(viewLifecycleOwner) { listOfProperties ->
            mProperties = listOfProperties

            // Synchronize local and remote databases if internet is available
            if (isInternetAvailable(requireContext())) {
                syncFirestoreWithRoomDatabases(mProperties)
            }

            // Define and configure adapter (MUST BE CALLED HERE FOR DATA REFRESH)
            adapter = ListAdapter(mProperties, requireContext())
            mRecyclerView.adapter = adapter
            setTextViews()
        }
    }

    // Synchronize properties data with local Room database & remote Firestore Database
    private fun syncFirestoreWithRoomDatabases(localProperties: MutableList<Property?>) {
        // Call remote firestore properties data
        PropertyManager.getAllPropertiesData().addOnCompleteListener { task ->
            val firebaseList = task.result
            val localList = mProperties

            if (firebaseList != localList) {
                // Check if remote properties are on local database
                for (firebaseProperty in firebaseList!!) {
                    // If remote property is not in Room Database
                    if (!localList.contains(firebaseProperty)) {
                        // If property is really absent, save it in Firebase Firestore, and update realtor account
                        if (firebaseProperty != null && !localList.any { it!!.id == firebaseProperty.id }) {
                            propertyViewModel.saveProperty(firebaseProperty)
                            UserManager.addPropertyToRealtorProperties(firebaseProperty.id.toString())
                        // else, it means that property has been updated
                        } else {
                            // check date of update
                            for (localProperty in localList) {
                                val localPropertyDateOfUpdate: Date? = if (localProperty!!.updateDate.isNotEmpty()) {
                                    convertStringToDate(localProperty.updateDate)
                                } else {
                                    Date(0)
                                }
                                val firebasePropertyDateOfUpdate: Date? =  if (firebaseProperty!!.updateDate.isNotEmpty()) {
                                    convertStringToDate(firebaseProperty.updateDate)
                                } else {
                                    Date(0)
                                }

                                if (localProperty.id == firebaseProperty.id) {
                                    // Update property in local or remote database according to last update
                                    if (localPropertyDateOfUpdate!!.before(firebasePropertyDateOfUpdate) ) {
                                        propertyViewModel.updateProperty(firebaseProperty)
                                    } else {
                                        PropertyManager.updateProperty(localProperty)
                                    }
                                }
                            }
                        }
                    }
                }
                // Check if local properties are on Firebase
                for (localProperty in localProperties) {
                    // If local property is not on Firebase, save it
                    if (!firebaseList.contains(localProperty)) {
                        // If property is really absent, save it in local Room database, and update realtor account
                        if (localProperty != null && !firebaseList.any { it!!.id == localProperty.id }) {
                            PropertyManager.createProperty(localProperty)
                            UserManager.addPropertyToRealtorProperties(localProperty.id.toString())
                            // Else, it means that property was updated
                        } else {
                            // check date of update
                            for (firebaseProperty in firebaseList) {
                                val localPropertyDateOfUpdate: Date? = if (localProperty!!.updateDate.isNotEmpty()) {
                                    convertStringToDate(localProperty.updateDate)
                                } else {
                                    Date(0)
                                }
                                val firebasePropertyDateOfUpdate: Date? =  if (firebaseProperty!!.updateDate.isNotEmpty()) {
                                    convertStringToDate(firebaseProperty.updateDate)
                                } else {
                                    Date(0)
                                }
                                if (localProperty.id == firebaseProperty.id) {
                                    // Update property in local or remote database according to last update
                                    if (localPropertyDateOfUpdate!!.before(firebasePropertyDateOfUpdate) ) {
                                        propertyViewModel.updateProperty(firebaseProperty)
                                    } else {
                                        PropertyManager.updateProperty(localProperty)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // Init the recyclerView that contains properties list
    private fun configureRecyclerView() {

        // Define layout & dividers
        mRecyclerView = fragmentView.findViewById(R.id.list_properties)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(divider)

    }

    // Set textViews according to properties list size
    private fun setTextViews() {
        mLabelNoProperty = fragmentView.findViewById(R.id.no_property_label)
        mFragmentTitle = fragmentView.findViewById(R.id.list_properties_title)
        if (mProperties.isEmpty()) {
            // Display "No property found"
            mLabelNoProperty.visibility = View.VISIBLE
            // Hide list title
            mFragmentTitle.visibility = View.GONE
        } else {
            // Display list title
            mFragmentTitle.visibility = View.VISIBLE
            // Hide "No property found"
            mLabelNoProperty.visibility = View.GONE
        }
    }
}