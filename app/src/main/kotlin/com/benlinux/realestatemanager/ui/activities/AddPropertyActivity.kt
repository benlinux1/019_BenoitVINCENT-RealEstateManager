package com.benlinux.realestatemanager.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.activities.AddPropertyActivity.Enum.Companion.PERMS
import com.benlinux.realestatemanager.ui.activities.AddPropertyActivity.Enum.Companion.RC_IMAGE_PERMS
import com.benlinux.realestatemanager.ui.adapters.PictureAdapter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.Realtor
import com.benlinux.realestatemanager.utils.*
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


open class AddPropertyActivity: AppCompatActivity() {

    // Property type radio buttons
    private lateinit var typeRadioGroup1: RadioGroup
    private lateinit var typeRadioGroup2: RadioGroup

    // Property pictures list
    private var picturesList: MutableList<Picture?> = mutableListOf()

    // Save button
    private lateinit var saveButton: Button

    private lateinit var addPictureButton: ImageView

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // Input fields & layouts
    private lateinit var titleLayout: TextInputLayout
    private lateinit var title: EditText
    private lateinit var areaLayout: TextInputLayout
    private lateinit var area: EditText
    private lateinit var priceLayout: TextInputLayout
    private lateinit var price: EditText
    private lateinit var surfaceLayout: TextInputLayout
    private lateinit var surface: EditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var description: EditText
    private lateinit var picturesRecyclerView: RecyclerView
    private lateinit var emptyRecyclerViewText: TextView
    private lateinit var addressTitle: TextView
    private lateinit var streetNumberLayout: TextInputLayout
    private lateinit var streetNumber: EditText
    private lateinit var streetNameLayout: TextInputLayout
    private lateinit var streetName: EditText
    private lateinit var addressComplementLayout: TextInputLayout
    private lateinit var addressComplement: EditText
    private lateinit var postalCodeLayout: TextInputLayout
    private lateinit var postalCode: EditText
    private lateinit var cityLayout: TextInputLayout
    private lateinit var city: EditText
    private lateinit var countryLayout: TextInputLayout
    private lateinit var country: EditText


    // The adapter which handles the list of pictures
    private lateinit var pictureAdapter: PictureAdapter

    // Created Property
    private var property: Property = Property()

    // Spinners
    private lateinit var roomSpinner: Spinner
    private lateinit var bedroomSpinner: Spinner
    private lateinit var bathroomSpinner: Spinner

    // Picture URL
    private var uriImageSelected: Uri? = null

    // Constants
    annotation class Enum {
        companion object {
            // Permissions for picture picking
            const val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE
            const val RC_IMAGE_PERMS = 100
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proprerty)

        setToolbar()
        setTypeRadioButtons()
        configureViewModel()
        setViews()
        setRoomsSpinners()
        setBedroomsSpinners()
        setBathroomsSpinners()
        setListenerOnCreateButton()
        setAddPictureButtonListener()
        configureRecyclerView()
    }

    // Toolbar configuration
    fun setToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        mToolbar.title = resources.getString(R.string.add_property_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Close add property activity and turn back to main activity if back button is clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Set radio buttons for property type
    private fun setTypeRadioButtons() {
        typeRadioGroup1 = findViewById(R.id.add_type_radioGroup1)
        typeRadioGroup2 = findViewById(R.id.add_type_radioGroup2)
        checkIfPropertyTypeIsChecked(typeRadioGroup1, typeRadioGroup2)
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define ViewModel
        val viewModelFactory = ViewModelFactory(this.application)
        propertyViewModel = ViewModelProvider(this, viewModelFactory)[PropertyViewModel::class.java]
    }

    // Set save button actions
    private fun setListenerOnCreateButton() {
        saveButton.setOnClickListener {
            if (checkPropertyFields()) {
                createProperty()
                propertyViewModel.saveProperty(property)
                Log.d("PROPERTY CREATED", property.toString())
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            } else {
                Log.d("PROPERTY NOT CREATED", "Not validated")
            }
        }
    }

    // Set all activity views
    private fun setViews() {
        titleLayout = findViewById(R.id.add_name_layout)
        title = findViewById(R.id.add_name_input)
        title.requestFocus()
        areaLayout = findViewById(R.id.add_area_layout)
        area = findViewById(R.id.add_area_input)
        priceLayout = findViewById(R.id.add_price_layout)
        price = findViewById(R.id.add_price_input)
        surfaceLayout = findViewById(R.id.add_surface_layout)
        surface = findViewById(R.id.add_surface_input)
        descriptionLayout = findViewById(R.id.add_description_layout)
        description = findViewById(R.id.add_description_text)
        picturesRecyclerView = findViewById(R.id.add_pictures_list)
        emptyRecyclerViewText = findViewById(R.id.empty_error_text)
        saveButton = findViewById(R.id.create)
        addPictureButton = findViewById(R.id.add_pictures_button)
        addressTitle = findViewById(R.id.add_address_layout_title)
        streetNumberLayout = findViewById(R.id.add_street_number_layout)
        streetNumber = findViewById(R.id.add_street_number_input)
        streetNameLayout = findViewById(R.id.add_street_name_layout)
        streetName = findViewById(R.id.add_street_name_input)
        addressComplementLayout = findViewById(R.id.add_address_complement_layout)
        addressComplement = findViewById(R.id.add_address_complement_input)
        postalCodeLayout = findViewById(R.id.add_postal_code_layout)
        postalCode = findViewById(R.id.add_postal_code_input)
        cityLayout = findViewById(R.id.add_city_layout)
        city = findViewById(R.id.add_city_input)
        countryLayout = findViewById(R.id.add_country_layout)
        country = findViewById(R.id.add_country_input)
    }

    // Create property action that retrieves all data
    private fun createProperty() {
        property.name = title.text.toString()
        property.area = area.text.toString()
        property.type = getPropertyType()
        getPropertyPrice()
        getPropertySurface()
        property.description = description.text.toString()
        property.isAvailable = isPropertyAvailable()
        property.creationDate = getTodayDate()
        property.pictures = picturesList
        // TODO : get current realtor
        property.realtor = Realtor(0, "", "", "", "", "")
        property.pictures = picturesList
        property.address = getPropertyAddress()
    }

    // Fields validation
    private fun checkPropertyFields(): Boolean {
        return (checkIfFieldsIsNotEmpty(areaLayout)
                && validateNumbers(price.text.toString(), priceLayout)
                && validateNumbers(surface.text.toString(), surfaceLayout)
                && checkIfFieldsIsNotEmpty(descriptionLayout)
                && validateField(title.text.toString(), titleLayout)
                && validateNumbers(streetNumber.text.toString(), streetNumberLayout)
                && validateField(streetName.text.toString(), streetNameLayout)
                && validateNumbers(postalCode.text.toString(), postalCodeLayout)
                && validateField(city.text.toString(), cityLayout)
                && validateField(country.text.toString(), countryLayout))
    }

    // Add picture action
    private fun setAddPictureButtonListener() {
        addPictureButton.setOnClickListener {
            updateAvatarPicture()
        }
    }

    // Rooms number spinner
    private fun setRoomsSpinners() {

        roomSpinner = findViewById(R.id.add_number_rooms_count)
        val roomsItems = arrayListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25, 26,27,28,29,30)
        val roomAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roomsItems)
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roomSpinner.adapter = roomAdapter
        roomSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedText = parent!!.getChildAt(0) as TextView
                selectedText.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.colorAccent))
                property.numberOfRooms = roomAdapter.getItem(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    // Bedrooms number spinner
    private fun setBedroomsSpinners() {
        bedroomSpinner = findViewById(R.id.add_number_bedrooms_count)
        val bedroomsItems = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        val bedroomAdapter = ArrayAdapter(
            this,
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
                val selectedText = parent!!.getChildAt(0) as TextView
                selectedText.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.colorAccent))
                property.numberOfBedrooms = bedroomAdapter.getItem(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    // Bathrooms number spinner
    private fun setBathroomsSpinners() {
        // Bathrooms spinner
        bathroomSpinner = findViewById(R.id.add_number_bathrooms_count)
        val bathroomsItems = arrayListOf(0,1,2,3,4,5,6,7,8,9,10)
        val bathroomAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bathroomsItems)
        bathroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bathroomSpinner.adapter = bathroomAdapter
        bathroomSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedText = parent!!.getChildAt(0) as TextView
                selectedText.setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.colorAccent))
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
            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonIDinGroup1)
            selectedRadioButton.text.toString()
        } else { // it means that button is checked in Group 2
            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonIDinGroup2)
            selectedRadioButton.text.toString()
        }
    }

    private fun getPropertyPrice() {
        val priceValue = price.text.toString()
        try {
            property.price = priceValue.toInt()
        } catch (e: NumberFormatException) {
            Log.d("ERROR PRICE CONVERSION", e.toString())
        }
    }

    private fun getPropertySurface() {
        val surfaceValue = surface.text.toString()
        try {
            property.surface = surfaceValue.toInt()
        } catch (e: NumberFormatException) {
            Log.d("ERROR SURFACE", e.toString())
        }
    }

    // Get property availability
    private fun isPropertyAvailable(): Boolean {
        val available: RadioButton = findViewById(R.id.add_status_radioButton1)
        return available.isChecked
    }

    // Easy permission result for photo access
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // When photo access is granted
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private fun updateAvatarPicture() {
        // Ask permission
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.allow_photo_access),
                RC_IMAGE_PERMS,
                PERMS
            )
            return
        }
        // When permission granted, allow picking action
        Toast.makeText(this, getString(R.string.picture_enabled), Toast.LENGTH_SHORT).show()
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        actionPick.launch(pickPhotoIntent)
    }

    // Create callback when user pick a photo on his device
    private val actionPick = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onPickPhotoResult(result) }

    // Handle result of photo picking activity
    private fun onPickPhotoResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) { //SUCCESS
            assert(result.data != null)
            this.uriImageSelected = result.data!!.data

            // Show dialog window for picture information
            result.data!!.data?.let {
                showAddPictureDialog(this, it
                )
            }

        } else {
            Toast.makeText(this, getString(R.string.no_image_chosen), Toast.LENGTH_SHORT).show()
        }
    }

    // When picture is selected from device, create dialog window
    private fun showAddPictureDialog(mContext: Context, imagePreview: Uri) {
        // Builder & custom view
        val builder = AlertDialog.Builder(mContext, R.style.CustomAlertDialog)
        val customView = layoutInflater.inflate(R.layout.custom_dialog_add_picture,null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Custom view picture
        val imageView: ImageView = customView.findViewById(R.id.property_new_picture_preview)
        // Custom view room name
        val roomName: EditText = customView.findViewById(R.id.add_room_name_input)
        roomName.requestFocus()
        // Negative button
        val negativeButton: Button = customView.findViewById(R.id.picture_dialog_negative_button)
        // Positive button
        val positiveButton: Button = customView.findViewById(R.id.picture_dialog_positive_button)

        // Picture preview
        Glide.with(this)
            .load(imagePreview)
            .apply(RequestOptions.centerInsideTransform())
            .into(imageView)

        // Positive button & actions
        positiveButton.setOnClickListener {
            // Picture Data Creation
            val picture = Picture(imagePreview.toString(), roomName.text.toString())
            // Add picture
            addPicture(picture)
            // Remove dialog window
            dialogWindow.dismiss()
            // Clear title focus to stay on pictures recyclerview
            title.clearFocus()
            roomSpinner.requestFocus()
        }

        // Negative button & actions
        negativeButton.setOnClickListener {
            // Picture Data Creation
            dialogWindow.cancel() }

        // Display dialog
        dialogWindow.show()
    }

    // Add picture to pictures list
    private fun addPicture(picture: Picture) {
        picturesList.add(picture)
        pictureAdapter.updatePictures(picturesList)
    }

    /**
     * Init the recyclerView that contains pictures list
     */
    private fun configureRecyclerView() {
        // Define layout & adapter
        picturesRecyclerView = findViewById(R.id.add_pictures_list)
        pictureAdapter = PictureAdapter(picturesList, this )
        picturesRecyclerView.adapter = pictureAdapter
    }

    // TODO : set address location with geocoder
    private fun getPropertyAddress(): PropertyAddress {
        return PropertyAddress(
                streetNumber.text.toString(),
                streetName.text.toString(),
                addressComplement.text.toString(),
                postalCode.text.toString(),
                city.text.toString(),
                country.text.toString()
        )
    }
}