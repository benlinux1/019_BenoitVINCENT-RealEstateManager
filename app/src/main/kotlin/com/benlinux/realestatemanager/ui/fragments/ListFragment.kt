package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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

    // Filter button
    private lateinit var filterButton: Button



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_list_view, container, false)
        configureViewModel()
        configureRecyclerView()
        setFilterButton()

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

    private fun setFilterButton() {
        filterButton = fragmentView.findViewById(R.id.list_filter_button)
        filterButton.setOnClickListener {
            launchFilterDialog()
        }
    }

    private fun launchFilterDialog() {

        // Builder & custom view
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val customView = layoutInflater.inflate(R.layout.custom_filters_layout,null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Validate filters button
        val validateFiltersButton: Button = customView.findViewById(R.id.filters_validate_button)

        // Views
        val flatType: CheckBox = customView.findViewById(R.id.filter_type_radiobutton_1)
        val houseType: CheckBox = customView.findViewById(R.id.filter_type_radiobutton_2)
        val duplexType: CheckBox = customView.findViewById(R.id.filter_type_radiobutton_3)
        val penthouseType: CheckBox = customView.findViewById(R.id.filter_type_radiobutton_4)
        val surfaceMin: EditText = customView.findViewById(R.id.filter_surface_min)
        val surfaceMax: EditText = customView.findViewById(R.id.filter_surface_max)
        val priceMin: EditText = customView.findViewById(R.id.filter_price_min)
        val priceMax: EditText = customView.findViewById(R.id.filter_price_max)
        val roomsMin: EditText = customView.findViewById(R.id.filter_rooms_min)
        val roomsMax: EditText = customView.findViewById(R.id.filter_rooms_max)
        val bedroomsMin: EditText = customView.findViewById(R.id.filter_bedrooms_min)
        val bedroomsMax: EditText = customView.findViewById(R.id.filter_bedrooms_max)
        val bathroomsMin: EditText = customView.findViewById(R.id.filter_bathrooms_min)
        val bathroomsMax: EditText = customView.findViewById(R.id.filter_bathrooms_max)
        val availableButton: RadioButton = customView.findViewById(R.id.filter_status_radioButton1)
        val soldButton: RadioButton = customView.findViewById(R.id.filter_status_radioButton2)
        val creationDateTitle: TextView = customView.findViewById(R.id.filter_creation_date_title)
        val creationDateMin: EditText = customView.findViewById(R.id.filter_creation_date_min)
        val creationDateMax: EditText = customView.findViewById(R.id.filter_creation_date_max)
        val updateDateTitle: TextView = customView.findViewById(R.id.filter_update_date_title)
        val updateDateMin: EditText = customView.findViewById(R.id.filter_update_date_min)
        val updateDateMax: EditText = customView.findViewById(R.id.filter_update_date_max)
        val soldDateTitle: TextView = customView.findViewById(R.id.filter_sold_date_title)
        val soldDateMin: EditText = customView.findViewById(R.id.filter_sold_date_min)
        val soldDateMax: EditText = customView.findViewById(R.id.filter_sold_date_max)

        // Set custom background design with radius and insets
        dialogWindow.window?.setBackgroundDrawableResource(R.drawable.background_filter_dialog)

        // validate filters button & actions
        validateFiltersButton.setOnClickListener {
            // TODO : launch search
            dialogWindow.dismiss()
        }

        // Display dialog
        dialogWindow.show()

    }
}