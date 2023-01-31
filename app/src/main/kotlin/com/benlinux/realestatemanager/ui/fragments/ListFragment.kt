package com.benlinux.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.view.Gravity
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
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.convertDateToString
import com.benlinux.realestatemanager.utils.convertStringToDate
import com.benlinux.realestatemanager.utils.convertStringToShortDate
import com.benlinux.realestatemanager.utils.isInternetAvailable
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class ListFragment: Fragment() {

    // The fragment root view
    private lateinit var fragmentView: View

    // The recycler view that contains properties
    private lateinit var mRecyclerView: RecyclerView

    // List of all current properties in the application
    private var mProperties: MutableList<Property?> = mutableListOf()

    // List of filtered properties
    private var filteredProperties: MutableList<Property?> = mutableListOf()

    // The adapter which handles the list of properties
    private lateinit var adapter: ListAdapter

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // TextViews
    private lateinit var mLabelNoProperty: TextView

    // Filter button
    private lateinit var filterButton: Button

    // Clear Filters button
    private lateinit var clearFiltersButton: Button



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_list_view, container, false)

        configureRecyclerView()
        configureViewModel()
        setFilterButton()
        setClearFiltersButton()

        return fragmentView
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
                propertyViewModel.syncLocalAndRemoteDatabases()
            }

            // Update properties in recycler view adapter
            adapter.updateProperties(mProperties)

            // Display / hide no property label according to situation
            setTextViews()
        }
    }


    // Init the recyclerView that contains properties list
    private fun configureRecyclerView() {

        // Define layout & dividers
        mRecyclerView = fragmentView.findViewById(R.id.list_properties)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(divider)
        adapter = ListAdapter(mProperties, requireContext())
        mRecyclerView.adapter = adapter
        mRecyclerView.setHasFixedSize(true)
    }


    // Set textViews according to properties list size
    private fun setTextViews() {
        mLabelNoProperty = fragmentView.findViewById(R.id.no_property_label)

        if (mProperties.isEmpty()) {
            // Display "No property found"
            mLabelNoProperty.visibility = View.VISIBLE

        } else {
            // Hide "No property found"
            mLabelNoProperty.visibility = View.GONE
        }
    }

    // Set filter button and actions
    private fun setFilterButton() {
        filterButton = fragmentView.findViewById(R.id.list_filter_button)
        filterButton.setOnClickListener {
            launchFilterDialog()
        }
    }

    // Set reset filters button and actions
    private fun setClearFiltersButton() {
        clearFiltersButton = fragmentView.findViewById(R.id.list_clear_filters_button)
        clearFiltersButton.setOnClickListener {
            adapter.updateProperties(mProperties)
            clearFiltersButton.visibility = View.GONE
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
        val surfaceMinInput: EditText = customView.findViewById(R.id.filter_surface_min)
        val surfaceMaxInput: EditText = customView.findViewById(R.id.filter_surface_max)
        val priceMinInput: EditText = customView.findViewById(R.id.filter_price_min)
        val priceMaxInput: EditText = customView.findViewById(R.id.filter_price_max)
        val roomsMinInput: EditText = customView.findViewById(R.id.filter_rooms_min)
        val roomsMaxInput: EditText = customView.findViewById(R.id.filter_rooms_max)
        val bedroomsMinInput: EditText = customView.findViewById(R.id.filter_bedrooms_min)
        val bedroomsMaxInput: EditText = customView.findViewById(R.id.filter_bedrooms_max)
        val bathroomsMinInput: EditText = customView.findViewById(R.id.filter_bathrooms_min)
        val bathroomsMaxInput: EditText = customView.findViewById(R.id.filter_bathrooms_max)
        val availableButton: RadioButton = customView.findViewById(R.id.filter_status_radioButton1)
        val creationDateTitleInput: TextView = customView.findViewById(R.id.filter_creation_date_title)
        val creationDateMinLayout: TextInputLayout = customView.findViewById(R.id.filter_creation_date_min_layout)
        val creationDateMaxLayout: TextInputLayout = customView.findViewById(R.id.filter_creation_date_max_layout)
        val creationDateMinInput: EditText = customView.findViewById(R.id.filter_creation_date_min)
        val creationDateMaxInput: EditText = customView.findViewById(R.id.filter_creation_date_max)
        val updateDateTitle: TextView = customView.findViewById(R.id.filter_update_date_title)
        val updateDateMinLayout: TextInputLayout = customView.findViewById(R.id.filter_update_date_min_layout)
        val updateDateMaxLayout: TextInputLayout = customView.findViewById(R.id.filter_update_date_max_layout)
        val updateDateMinInput: EditText = customView.findViewById(R.id.filter_update_date_min)
        val updateDateMaxInput: EditText = customView.findViewById(R.id.filter_update_date_max)
        val soldDateTitle: TextView = customView.findViewById(R.id.filter_sold_date_title)
        val soldDateMinLayout: TextInputLayout = customView.findViewById(R.id.filter_sold_date_min_layout)
        val soldDateMaxLayout: TextInputLayout = customView.findViewById(R.id.filter_sold_date_max_layout)
        val soldDateMinInput: EditText = customView.findViewById(R.id.filter_sold_date_min)
        val soldDateMaxInput: EditText = customView.findViewById(R.id.filter_sold_date_max)

        // Set custom background design with radius and insets
        dialogWindow.window?.setBackgroundDrawableResource(R.drawable.background_filter_dialog)

        // Status
        val isAvailable = availableButton.isChecked

        // Set dates types according to property's required status
        val statusGroup: RadioGroup = customView.findViewById(R.id.filter_status_radioGroup)
        statusGroup.setOnCheckedChangeListener { _, _ ->
            if (availableButton.isChecked) {
                creationDateTitleInput.visibility = View.VISIBLE
                creationDateMinLayout.visibility = View.VISIBLE
                creationDateMaxLayout.visibility = View.VISIBLE
                updateDateTitle.visibility = View.VISIBLE
                updateDateMinLayout.visibility = View.VISIBLE
                updateDateMaxLayout.visibility = View.VISIBLE
                soldDateTitle.visibility = View.GONE
                soldDateMinLayout.visibility = View.GONE
                soldDateMaxLayout.visibility = View.GONE
            } else {
                creationDateTitleInput.visibility = View.GONE
                creationDateMinLayout.visibility = View.GONE
                creationDateMaxLayout.visibility = View.GONE
                updateDateTitle.visibility = View.GONE
                updateDateMinLayout.visibility = View.GONE
                updateDateMaxLayout.visibility = View.GONE
                soldDateTitle.visibility = View.VISIBLE
                soldDateMinLayout.visibility = View.VISIBLE
                soldDateMaxLayout.visibility = View.VISIBLE
            }
        }

        // Set listeners when user click on date field
        setListenerOnDateField(creationDateMinInput, getString(R.string.creation_date_dialog_title))
        setListenerOnDateField(creationDateMaxInput, getString(R.string.creation_date_dialog_title))
        setListenerOnDateField(updateDateMinInput, getString(R.string.update_date_dialog_title))
        setListenerOnDateField(updateDateMaxInput, getString(R.string.update_date_dialog_title))
        setListenerOnDateField(soldDateMinInput, getString(R.string.sold_date_dialog_title))
        setListenerOnDateField(soldDateMinInput, getString(R.string.sold_date_dialog_title))

        // validate filters button & actions
        validateFiltersButton.setOnClickListener {

            // Set empty filtered list
            filteredProperties = mutableListOf()

            // Surface
            var surfaceMin = 0
            if (surfaceMinInput.text.isNotEmpty()) { surfaceMin = surfaceMinInput.text.toString().toInt() }
            var surfaceMax = 999999999
            if (surfaceMaxInput.text.isNotEmpty()) { surfaceMax = surfaceMaxInput.text.toString().toInt() }
            // Price
            var priceMin = 0
            if (priceMinInput.text.isNotEmpty()) { priceMin = priceMinInput.text.toString().toInt() }
            var priceMax = 999999999
            if (priceMaxInput.text.isNotEmpty()) { priceMax = priceMaxInput.text.toString().toInt() }
            // Number of rooms
            var roomMin = 0
            if (roomsMinInput.text.isNotEmpty()) { roomMin = roomsMinInput.text.toString().toInt() }
            var roomMax = 999999999
            if (roomsMaxInput.text.isNotEmpty()) { roomMax = roomsMaxInput.text.toString().toInt() }
            // Number of bedrooms
            var bedroomMin = 0
            if (bedroomsMinInput.text.isNotEmpty()) { bedroomMin = bedroomsMinInput.text.toString().toInt() }
            var bedroomMax = 999999999
            if (bedroomsMaxInput.text.isNotEmpty()) { bedroomMax = bedroomsMaxInput.text.toString().toInt() }
            // Number of bathrooms
            var bathroomMin = 0
            if (bathroomsMinInput.text.isNotEmpty()) { bathroomMin = bathroomsMinInput.text.toString().toInt() }
            var bathroomMax = 999999999
            if (bathroomsMaxInput.text.isNotEmpty()) { bathroomMax = roomsMaxInput.text.toString().toInt() }

            // All dates
            var dateOfPublicationMin: Date? = null
            var dateOfPublicationMax: Date? = null
            var dateOfUpdateMin: Date? = null
            var dateOfUpdateMax: Date? = null
            var dateOfSoldMin: Date? = null
            var dateOfSoldMax: Date? = null


            // Status & dates
            if (isAvailable) {
                if (creationDateMinInput.text.isNotEmpty()) { dateOfPublicationMin = convertStringToShortDate(creationDateMinInput.text.toString()) }
                if (creationDateMaxInput.text.isNotEmpty()) { dateOfPublicationMax = convertStringToShortDate(creationDateMaxInput.text.toString()) }
                if (updateDateMinInput.text.isNotEmpty()) { dateOfUpdateMin = convertStringToDate(updateDateMinInput.text.toString()) }
                if (updateDateMaxInput.text.isNotEmpty()) { dateOfUpdateMax = convertStringToDate(updateDateMaxInput.text.toString()) }
            } else {
                if (soldDateMinInput.text.isNotEmpty()) { dateOfSoldMin = convertStringToShortDate(soldDateMinInput.text.toString()) }
                if (soldDateMaxInput.text.isNotEmpty()) { dateOfSoldMax = convertStringToShortDate(soldDateMaxInput.text.toString()) }
            }

            // Types (translation added)
            val types = arrayListOf<String>()
            if (flatType.isChecked) { types.add(getString(R.string.add_type_flat)) && types.add(getString(R.string.add_type_flat_trad)) }
            if (houseType.isChecked) { types.add(getString(R.string.add_type_house)) && types.add(getString(R.string.add_type_house_trad)) }
            if (duplexType.isChecked) { types.add(getString(R.string.add_type_duplex)) && types.add(getString(R.string.add_type_duplex_trad)) }
            if (penthouseType.isChecked) { types.add(getString(R.string.add_type_penthouse)) && types.add(getString(R.string.add_type_penthouse_trad)) }

            // Filter results
            filterResults(
                types,
                surfaceMin,
                surfaceMax,
                priceMin,
                priceMax,
                roomMin,
                roomMax,
                bedroomMin,
                bedroomMax,
                bathroomMin,
                bathroomMax,
                dateOfPublicationMin,
                dateOfPublicationMax,
                dateOfUpdateMin,
                dateOfUpdateMax,
                dateOfSoldMin,
                dateOfSoldMax
            )
            dialogWindow.dismiss()
        }
        // Display dialog
        dialogWindow.show()
    }

    // When user clicks on date input, launch date dialog with custom title
    private fun setListenerOnDateField(dateField: EditText, title: String) {
        dateField.setOnFocusChangeListener { view, _ ->
            if (view.isFocused) {
                showDateDialog(dateField, title)
            }
        }
    }

    // Filter displayed list according to input fields data
    private fun filterResults(
        types: ArrayList<String>,
        surfaceMin: Int?,
        surfaceMax: Int?,
        priceMin: Int?,
        priceMax: Int?,
        numberOfRoomsMin: Int?,
        numberOfRoomsMax: Int?,
        numberOfBedroomsMin: Int?,
        numberOfBedroomsMax: Int?,
        numberOfBathroomsMin: Int?,
        numberOfBathroomsMax: Int?,
        creationDateMin: Date?,
        creationDateMax: Date?,
        updateDateMin: Date?,
        updateDateMax: Date?,
        soldDateMin: Date?,
        soldDateMax: Date?
    ) {
        for (property in mProperties) {

            // Date conversion
            val creationDate = if (property?.creationDate?.isNotEmpty() == true) {
                convertStringToShortDate(property.creationDate)
            } else { null }

            val updateDate = if (property?.updateDate?.isNotEmpty() == true) {
                 convertStringToShortDate(property.updateDate)
            } else { null }

            val soldDate = if (property?.soldDate?.isNotEmpty() == true) {
                convertStringToShortDate(property.soldDate!!)
            } else { null }

            // Check dates
            var creationMin = true
            if (creationDateMin != null && creationDate != null) { creationMin = checkMinDate(creationDateMin, creationDate) }
            var creationMax = true
            if (creationDateMax != null && creationDate != null) { creationMax = checkMaxDate(creationDateMax, creationDate) }
            var updateMin = true
            if (updateDateMin != null && updateDate != null) { updateMin = checkMinDate(updateDateMin, updateDate) }
            var updateMax = true
            if (updateDateMax != null && updateDate != null) { updateMax = checkMaxDate(updateDateMax, updateDate) }
            var soldMin = true
            if (soldDateMin != null && soldDate != null) { soldMin = checkMinDate(soldDateMin, soldDate) }
            var soldMax = true
            if (soldDateMax != null && soldDate != null) { soldMax = checkMaxDate(soldDateMax, soldDate) }

            // Check all filters
            if (property?.surface!! in surfaceMin!!..surfaceMax!!
                && property.price in priceMin!!..priceMax!!
                && property.numberOfRooms in numberOfRoomsMin!!..numberOfRoomsMax!!
                && property.numberOfBedrooms in numberOfBedroomsMin!!..numberOfBedroomsMax!!
                && property.numberOfBathrooms in numberOfBathroomsMin!!..numberOfBathroomsMax!!
                && types.contains(property.type)
                && creationMin
                && creationMax
                && updateMin
                && updateMax
                && soldMin
                && soldMax
            ){
                // if filters concord, add property to list
                filteredProperties.add(property)
            }
        }
        // update list
        adapter.updateProperties(filteredProperties)
        // Add clear filters button
        clearFiltersButton.visibility = View.VISIBLE
    }

    private fun checkMinDate(request: Date?, final: Date?): Boolean {
        return request!!.before(final)
    }

    private fun checkMaxDate(request: Date?, final: Date?): Boolean {
        return request!!.after(final)
    }

    // Custom date picker dialog
    @Suppress("DEPRECATION")
    private fun showDateDialog(destination: EditText, title: String) {
        val currentDate = Calendar.getInstance(Locale.FRANCE)
        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

        // Date Picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), object : DatePickerDialog.OnDateSetListener {
                private val date = Calendar.getInstance(Locale.FRANCE)

                // When user select a date
                override fun onDateSet(
                    datePicker: DatePicker,
                    year: Int,
                    monthOfYear: Int,
                    dayOfMonth: Int
                ) {
                    date[year, monthOfYear] = dayOfMonth

                    // Set date into destination input field
                    destination.text = convertDateToString(date.time).toEditable()
                    destination.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH],
            currentDate[Calendar.DATE]
        )

        // Disable dates after today in date picker
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Customize title view
        val customTitle = TextView(requireContext())
        customTitle.text = title // title text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customTitle.setTextColor(resources.getColor(R.color.colorWhite, resources.newTheme())) // text color
            customTitle.setBackgroundColor(resources.getColor(R.color.colorAccent, resources.newTheme())) // background color
        } else {
            customTitle.setTextColor(resources.getColor(R.color.colorWhite)) // text color
            customTitle.setBackgroundColor(resources.getColor(R.color.colorAccent)) // background color
        }
        customTitle.gravity = Gravity.CENTER // gravity
        customTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19f) // text size
        customTitle.setPadding(8, 24, 8, 16) // padding
        // Set custom title
        datePickerDialog.setCustomTitle(customTitle)

        // Show date dialog
        datePickerDialog.show()
    }
}