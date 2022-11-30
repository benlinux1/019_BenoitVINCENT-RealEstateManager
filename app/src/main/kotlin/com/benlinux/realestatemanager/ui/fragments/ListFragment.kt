package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration

class ListFragment: Fragment() {

    private lateinit var fragmentView: View
    // The recycler view that contains properties
    private lateinit var mRecyclerView: RecyclerView
    // List of all current properties of the application
    private var mProperties = mutableListOf<Property>()
    // The adapter which handles the list of properties
    private lateinit var adapter: ListAdapter
    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_list_view, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        configRecyclerView()
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define activity (for context)
         val activity = requireNotNull(this.activity)

        // Define ViewModel
         propertyViewModel = ViewModelProvider(this,
             ViewModelFactory(activity.application))[PropertyViewModel::class.java]

        // Init ViewModel
        propertyViewModel.init()

    }


    /**
     * Init the recyclerView that contains properties list
     */
    private fun configRecyclerView() {

        // Create property example
        val propertyExample = Property(
            id =6, name ="Ma super villa", type ="flat", price = 1200000, area ="Quartier de dingue",
            realtor = Realtor(1, "***", "Ben", "Linux", ""), isAvailable = true )

        // Add property to viewModel list
        propertyViewModel.createProperty(propertyExample)

        // Add example to arrayList
        //mProperties.add(propertyExample)

        // Get properties in fragment
        mProperties = (propertyViewModel.getPropertiesList().value as MutableList<Property>?)!!

        // Add layout & dividers
        mRecyclerView = fragmentView.findViewById(R.id.list_properties)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(divider)

        // Configure adapter
        adapter = ListAdapter(mProperties)
        mRecyclerView.adapter = adapter

    }
}