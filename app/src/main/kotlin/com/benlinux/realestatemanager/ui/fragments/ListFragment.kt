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
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration

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
            // Synchronize local and remote databases
            // TODO: Connectivity Manager that allow this sync
            syncFirestoreWithRoomDatabases(mProperties)

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
            if (task.isSuccessful) {
                val list = task.result
                // Check if local properties are on Firebase
                for (localProperty in localProperties) {
                    // If local properties are not on Firebase, save each
                    if (!list!!.contains(localProperty)) {
                        if (localProperty != null) {
                            PropertyManager.createProperty(localProperty)
                        }
                    }
                }

                // Check if remote properties are on local database
                for (firebaseProperty in list!!) {
                    // If remote properties are not on Room Database, save each
                    if (!localProperties.contains(firebaseProperty)) {
                        if (firebaseProperty != null) {
                            propertyViewModel.saveProperty(firebaseProperty)
                        }
                    }
                }
            }
        }
    }


    /**
     * Init the recyclerView that contains properties list
     */
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