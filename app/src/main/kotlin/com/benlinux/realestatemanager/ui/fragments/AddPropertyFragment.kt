package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.checkIfFieldsAreNotEmpty
import com.benlinux.realestatemanager.utils.checkIfPropertyTypeIsChecked
import com.benlinux.realestatemanager.utils.getTodayDate
import com.benlinux.realestatemanager.utils.validateField
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.material.textfield.TextInputLayout
import java.util.Date


class AddPropertyFragment: Fragment() {

    private lateinit var fragmentView: View
    private lateinit var typeRadioGroup1: RadioGroup
    private lateinit var typeRadioGroup2: RadioGroup

    private lateinit var statusGroup: RadioGroup

    private var picturesList: List<Picture> = mutableListOf()
    private lateinit var saveButton: Button

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    private lateinit var titleLayout: TextInputLayout
    private lateinit var title: EditText
    private lateinit var area: TextInputLayout
    private lateinit var price: TextInputLayout
    private lateinit var surface: TextInputLayout
    private lateinit var description: TextInputLayout
    private lateinit var picturesRecyclerView: RecyclerView
    private lateinit var emptyRecyclerViewText: TextView

    private var property: Property = Property()

    private lateinit var roomSpinner: Spinner
    private lateinit var bedroomSpinner: Spinner
    private lateinit var bathroomSpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_property_view, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRadioButtons()
        configureViewModel()
        setViews()
        setRoomsSpinners()
        setBedroomsSpinners()
        setBathroomsSpinners()
        setListenerOnCreateButton()
    }

    private fun setRadioButtons() {
        typeRadioGroup1 = fragmentView.findViewById(R.id.add_type_radioGroup1)
        typeRadioGroup2 = fragmentView.findViewById(R.id.add_type_radioGroup2)
        checkIfPropertyTypeIsChecked(typeRadioGroup1, typeRadioGroup2)
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define ViewModel
        propertyViewModel = ViewModelProvider(this,
            ViewModelFactory.getInstance(requireContext())!!
        )[PropertyViewModel::class.java]
    }

    private fun setListenerOnCreateButton() {
        saveButton.setOnClickListener {
            if (checkPropertyFields()) {
                createProperty()
                propertyViewModel.saveProperty(property)
            } else {

            }
        }
    }


    private fun setViews() {
        titleLayout = fragmentView.findViewById(R.id.add_name_layout)
        title = fragmentView.findViewById(R.id.add_name_input)
        area = fragmentView.findViewById(R.id.add_area_layout)
        price = fragmentView.findViewById(R.id.add_price_layout)
        surface = fragmentView.findViewById(R.id.add_surface_layout)
        description = fragmentView.findViewById(R.id.add_description_layout)
        picturesRecyclerView = fragmentView.findViewById(R.id.add_pictures_list)
        emptyRecyclerViewText = fragmentView.findViewById(R.id.empty_error_text)
        saveButton = fragmentView.findViewById(R.id.create)
    }

    private fun createProperty() {
        property.name = titleLayout.editText.toString()
        property.area = area.editText.toString()
        property.type = getPropertyType()
        getPropertyPrice()
        getPropertySurface()
        property.description = description.editText.toString()
        property.isAvailable = isPropertyAvailable()
        property.creationDate = getTodayDate()
        property.pictures = picturesList
    }


    private fun checkPropertyFields(): Boolean {
        return (checkIfFieldsAreNotEmpty(area, price, surface, description, picturesRecyclerView, emptyRecyclerViewText )
            && validateField(title.toString(), titleLayout))
    }

    private fun addPicture(picture: Picture) {

    }

    private fun setRoomsSpinners() {
        // Room spinner
        roomSpinner = fragmentView.findViewById(R.id.add_number_rooms_count)
        val roomsItems = arrayListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)
        val roomAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roomsItems)
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roomSpinner.adapter = roomAdapter
        roomSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                property.numberOfRooms = roomAdapter.getItem(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setBedroomsSpinners() {
        // Bedrooms spinner
        bedroomSpinner = fragmentView.findViewById(R.id.add_number_bedrooms_count)
        val bedroomsItems = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        val bedroomAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            bedroomsItems
        )
        bedroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bedroomSpinner.adapter = bedroomAdapter
        bedroomSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                property.numberOfBedrooms = bedroomAdapter.getItem(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setBathroomsSpinners() {
        // Bathrooms spinner
        bathroomSpinner = fragmentView.findViewById(R.id.add_number_bathrooms_count)
        val bathroomsItems = arrayListOf(0,1,2,3,4,5,6,7,8,9,10)
        val bathroomAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bathroomsItems)
        bathroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bathroomSpinner.adapter = bathroomAdapter
        bathroomSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                property.numberOfBathrooms = bathroomAdapter.getItem(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getPropertyType(): String {
        val selectedRadioButtonIDinGroup1: Int = typeRadioGroup1.checkedRadioButtonId
        val selectedRadioButtonIDinGroup2: Int = typeRadioGroup2.checkedRadioButtonId

        // If nothing is selected from Radio Group, then it return -1
        return if (selectedRadioButtonIDinGroup1 != -1) {
            val selectedRadioButton: RadioButton = fragmentView.findViewById(selectedRadioButtonIDinGroup1)
            selectedRadioButton.text.toString()
        } else { // it means that button is checked in Group 2
            val selectedRadioButton: RadioButton = fragmentView.findViewById(selectedRadioButtonIDinGroup2)
            selectedRadioButton.text.toString()
        }
    }

    private fun getPropertyPrice() {
        val priceValue = price.editText.toString()
        try {
            property.price = priceValue.toInt()
        } catch (e: NumberFormatException) {
            Log.d("ERROR PRICE CONVERSION", e.toString())
        }
    }

    private fun getPropertySurface() {
        val surfaceValue = surface.editText.toString()
        try {
            property.surface = surfaceValue.toInt()
        } catch (e: NumberFormatException) {
            Log.d("ERROR SURFACE", e.toString())
        }
    }

    private fun isPropertyAvailable(): Boolean {
        val available: RadioButton = fragmentView.findViewById(R.id.add_status_radioGroup)
        return available.isChecked
    }
}

