package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.dao.PropertyDao
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.repository.PropertyRepository
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor
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
        configRecyclerView()
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextViews()
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {

        // Define activity (for context)
         val activity = requireNotNull(this.activity)

        // Define ViewModel
         propertyViewModel = ViewModelProvider(this,
             ViewModelFactory.getInstance(activity.applicationContext)!!
         )[PropertyViewModel::class.java]

        // Set observer on properties list
        propertyViewModel.getPropertiesList().observe(viewLifecycleOwner) {
            mProperties = it
        }
    }


    /**
     * Init the recyclerView that contains properties list
     */
    private fun configRecyclerView() {

        // Define layout & dividers
        mRecyclerView = fragmentView.findViewById(R.id.list_properties)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(divider)

        // Define and configure adapter
        adapter = ListAdapter(mProperties)
        mRecyclerView.adapter = adapter
    }

    // Set textViews according to properties list size
    private fun setTextViews() {
        mLabelNoProperty = fragmentView.findViewById(R.id.no_property_label)
        mFragmentTitle = fragmentView.findViewById(R.id.list_properties_title)
        if (mProperties.size == 0) {
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