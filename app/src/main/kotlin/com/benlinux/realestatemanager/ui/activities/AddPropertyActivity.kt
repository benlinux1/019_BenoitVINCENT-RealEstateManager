package com.benlinux.realestatemanager.ui.activities

import android.Manifest
import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
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
import com.benlinux.realestatemanager.data.propertyManager.PropertyManager
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.PictureAdapter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import com.benlinux.realestatemanager.utils.*
import com.benlinux.realestatemanager.utils.Constants.Companion.CAMERA_ACCESS_PERMISSION
import com.benlinux.realestatemanager.utils.Constants.Companion.IMAGE_CAPTURE_CODE
import com.benlinux.realestatemanager.utils.Constants.Companion.PHOTO_ACCESS_PERMISSION
import com.benlinux.realestatemanager.utils.Constants.Companion.RC_IMAGE_PERMS
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class AddPropertyActivity: AppCompatActivity() {

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

    // Current realtor
    private var realtor: User? = null
    private var userId: String? = null

    // Property status checkboxes
    private lateinit var availableButton: RadioButton
    private lateinit var soldButton: RadioButton

    // Date of sold
    private var dateOfSold: String? = null

    // Notification permission
    private var notificationPermissionIsGranted = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proprerty)

        checkNotificationPermissions()
        setToolbar()
        checkUserIdInSharedPreferences()
        getCurrentRealtor()
        setTypeRadioButtons()
        configureViewModel()
        setViews()
        setRoomsSpinners()
        setBedroomsSpinners()
        setBathroomsSpinners()
        setListenerOnCreateButton()
        setAddPictureButtonListener()
        configureRecyclerView()
        setListenerOnSoldChecked()
    }

    private fun getCurrentRealtor() {
        UserManager.getUserData()?.addOnSuccessListener { user ->
            if (user != null) {
                realtor = User(
                    user.id, user.email, user.firstName, user.lastName,
                    user.avatarUrl, user.favorites, user.isRealtor, user.realtorProperties
                )
            }
        }
        if (realtor == null) {
            UserManager.getUserDataById(userId!!).addOnSuccessListener { userById ->
                Log.d("REALTOR BY ID", userById.toString())
                if (userById != null) {
                    realtor = User(
                        userById.id, userById.email, userById.firstName, userById.lastName,
                        userById.avatarUrl, userById.favorites, userById.isRealtor, userById.realtorProperties
                    )
                }
            }
        }
    }

    // Toolbar configuration
    private fun setToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = resources.getString(R.string.add_property_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    // Close current activity and turn back to main activity if back button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Set radio buttons for property type (flat, penthouse, house, duplex)
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
            // If no errors
            if (checkPropertyFields()) {
                // Create property object with data in fields
                collectPropertyData()

                // save property in local Room database
                propertyViewModel.saveProperty(property)
                Log.d("PROPERTY CREATED", property.toString())

                // Send notification
                val propertyDetailsIntent = Intent(this, PropertyDetailsActivity::class.java)
                propertyDetailsIntent.putExtra("PROPERTY_ID", property.id.toString())
                propertyDetailsIntent.putExtra("PROPERTY_CREATOR_ID", realtor!!.id)
                sendCreationNotification(property.name,
                    this, notificationPermissionIsGranted,
                    propertyDetailsIntent
                )

                // Update realtor properties list
                if (isInternetAvailable(this)) {
                    UserManager.addPropertyToRealtorProperties(property.id.toString())
                }

                // Go to Main Activity
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
        availableButton = findViewById(R.id.add_status_radioButton1)
        soldButton = findViewById(R.id.add_status_radioButton2)
    }

    // Create property action that retrieves all data
    private fun collectPropertyData() {
        property.name = title.text.toString()
        property.area = area.text.toString()
        property.type = getPropertyType()
        getPropertyPrice()
        getPropertySurface()
        property.description = description.text.toString()
        property.isAvailable = isPropertyAvailable()
        property.creationDate = getTodayDate()
        property.updateDate = getTodayDate()
        if (!property.isAvailable) {
            property.soldDate = dateOfSold
        }
        property.realtor = realtor!!
        property.address = getPropertyAddress()
        property.id = System.currentTimeMillis().toInt()
        property.pictures = picturesList
    }

    private fun checkUserIdInSharedPreferences() {
        val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "")
        userId?.let { Log.d("USER ID", it) }
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

    // Rooms number spinner
    private fun setRoomsSpinners() {
        roomSpinner = findViewById(R.id.add_number_rooms_count)
        val roomsItems = arrayListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25, 26,27,28,29,30)
        val roomAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roomsItems)
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roomSpinner.adapter = roomAdapter
        // Listener on selected item to update view
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
        // Listener on selected item to update view
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
        // Listener on selected item to update view
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

    // Get property type according to checkbox clicked
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

    // Get price value
    private fun getPropertyPrice() {
        val priceValue = price.text.toString()
        try {
            property.price = priceValue.toInt()
        } catch (e: NumberFormatException) {
            Log.d("ERROR PRICE CONVERSION", e.toString())
        }
    }

    // Get surface value
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

    // Add picture action, show selector between gallery or camera
    private fun setAddPictureButtonListener() {
        addPictureButton.setOnClickListener {
            showMediaSelectorDialog()
        }
    }

    // Show dialog to choose between Gallery or Take Photo actions
    private fun showMediaSelectorDialog() {
        // Builder & custom view
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val customView = layoutInflater.inflate(R.layout.custom_dialog_media_selector,null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Gallery button
        val galleryButton: ImageView = customView.findViewById(R.id.gallery_button)
        // Camera Button
        val cameraButton: ImageView = customView.findViewById(R.id.camera_button)

        // Gallery button & actions
        galleryButton.setOnClickListener {
            updatePropertyPicture()
            dialogWindow.dismiss()
        }

        // Camera button & actions
        cameraButton.setOnClickListener {
            takePhoto()
            dialogWindow.dismiss()
        }

        // Display dialog
        dialogWindow.show()
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
    fun updatePropertyPicture() {
        // Ask permission (used for API 32 and less)
        if (Build.VERSION.SDK_INT <= 32) {
            if (!EasyPermissions.hasPermissions(this, PHOTO_ACCESS_PERMISSION)) {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.allow_photo_access),
                    RC_IMAGE_PERMS,
                    PHOTO_ACCESS_PERMISSION
                )
                return
            }
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

    // Permission request for Camera and actions
    @AfterPermissionGranted(IMAGE_CAPTURE_CODE)
    private fun takePhoto() {
        // Permission request for camera
        if (!EasyPermissions.hasPermissions(this, CAMERA_ACCESS_PERMISSION)) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.allow_camera_access),
                IMAGE_CAPTURE_CODE,
                CAMERA_ACCESS_PERMISSION
            )
            return

        }
        // When permission granted, allow camera action
        // Content values to get uri with content resolver builder options to write captured image to MediaStore
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Token picture")
        uriImageSelected = this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // Create camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Add uri to extras
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImageSelected)
        actionCamera.launch(cameraIntent) // Launch intent
    }


    // Create callback when user take a photo on his device
    private val actionCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onTakePhotoResult(result) }


    // Handle result of photo capture with device's camera
    private fun onTakePhotoResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) { //SUCCESS
            // Show dialog window for picture information
            showAddPictureDialog(this, uriImageSelected!!)
        } else {
            Toast.makeText(this, getString(R.string.no_image_chosen), Toast.LENGTH_SHORT).show()
        }
    }

    // When picture is selected from device, create dialog window
    private fun showAddPictureDialog(mContext: Context, imageUri: Uri) {
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
        val roomNameLayout: TextInputLayout = customView.findViewById(R.id.add_name_layout)
        // Negative button
        val negativeButton: Button = customView.findViewById(R.id.picture_dialog_negative_button)
        // Positive button
        val positiveButton: Button = customView.findViewById(R.id.picture_dialog_positive_button)

        // Picture preview
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions.centerInsideTransform())
            .into(imageView)

        // Positive button & actions
        positiveButton.setOnClickListener {
            // Picture Data Creation
            if (roomName.text.toString().length < 13) {
                PropertyManager.uploadImageToFirestore(imageUri).addOnSuccessListener {
                    // get download url
                    it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        // create picture with download url
                        val picture = Picture(uri.toString(), roomName.text.toString())
                        // Add picture to pictures list
                        addPicture(picture)
                        // Remove dialog window
                        dialogWindow.dismiss()
                        // Clear input focus to stay on pictures recyclerview
                        clearAllFocuses()
                        roomSpinner.requestFocus()
                    }
                }
            } else {
                roomNameLayout.error = getString(R.string.room_name_length_error)
            }
        }

        // Negative button & actions
        negativeButton.setOnClickListener {
            // Picture Data Creation
            dialogWindow.cancel() }

        // Display dialog
        dialogWindow.show()
    }

    // Clear inputs focus
    private fun clearAllFocuses() {
        title.clearFocus()
        area.clearFocus()
        price.clearFocus()
        surface.clearFocus()
        description.clearFocus()
        streetNumber.clearFocus()
        streetName.clearFocus()
        addressComplement.clearFocus()
        postalCode.clearFocus()
        city.clearFocus()
        country.clearFocus()
    }

    // Add picture to pictures list
    private fun addPicture(picture: Picture) {
        picturesList.add(picture)
        pictureAdapter.updatePictures(picturesList)
    }

    // Init the recyclerView that contains pictures list
    private fun configureRecyclerView() {
        // Define layout & adapter
        picturesRecyclerView = findViewById(R.id.add_pictures_list)
        pictureAdapter = PictureAdapter(picturesList, this, true )
        picturesRecyclerView.adapter = pictureAdapter
        setEasyScrollFeature()
    }

    // Show arrows on list sides to indicate scroll possibility
    private fun setEasyScrollFeature() {
        val arrowRight: ImageView = findViewById(R.id.list_arrow_right)
        val arrowLeft: ImageView = findViewById(R.id.list_arrow_left)

        // Set right arrow visibility
        if (picturesRecyclerView.canScrollHorizontally(1)) {
            arrowRight.visibility = View.VISIBLE
        }

        // Go to end of pictures list
        arrowRight.setOnClickListener {
            picturesRecyclerView.smoothScrollToPosition(picturesList.size)
        }
        // Go to start of pictures list
        arrowLeft.setOnClickListener {
            picturesRecyclerView.smoothScrollToPosition(0)
        }

        // After scroll, handle arrows visibility
        picturesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("scroll", "scrolling")
                if (!recyclerView.canScrollHorizontally(1)) {
                    arrowRight.visibility = View.GONE
                } else {
                    arrowRight.visibility = View.VISIBLE
                }
                if (!recyclerView.canScrollHorizontally(-1)) {
                    arrowLeft.visibility = View.GONE
                } else {
                    arrowLeft.visibility = View.VISIBLE
                }
            }
        })
    }

    // Create property Address
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

    // When user selects sold status, launch date picker
    private fun setListenerOnSoldChecked() {
        soldButton.setOnClickListener {
            showDateDialog()
        }
    }

    // Custom date picker dialog
    @Suppress("DEPRECATION")
    private fun showDateDialog() {
        val currentDate = Calendar.getInstance(Locale.FRANCE)

        // Date Picker dialog
        val datePickerDialog = DatePickerDialog(
            this, object : DatePickerDialog.OnDateSetListener {
                private val date = Calendar.getInstance(Locale.FRANCE)

                // When user select a date
                override fun onDateSet(
                    datePicker: DatePicker,
                    year: Int,
                    monthOfYear: Int,
                    dayOfMonth: Int
                ) {
                    date[year, monthOfYear] = dayOfMonth

                    // Define date of sold
                    dateOfSold = convertDateToString(date.time)
                }
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH],
            currentDate[Calendar.DATE]
        )

        // Disable dates after today in date picker
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Customize title view
        val customTitle = TextView(applicationContext)
        customTitle.setText(R.string.sold_date_dialog_title) // title text
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

        // When user cancel or dismiss date dialog
        datePickerDialog.setOnCancelListener {
            soldButton.isChecked = false
            availableButton.isChecked = true
        }

        // Show date dialog
        datePickerDialog.show()
    }

    // Check for location permission
    private fun checkNotificationPermissions() {
        // Callback for notification permission
        if (Build.VERSION.SDK_INT <= 33) {
            val requestPermission =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

                    if (isGranted) { // If permission is granted
                        notificationPermissionIsGranted = true
                        Log.d("LOG_TAG", "notification permission granted by the user")

                    } else { // If permission is not granted
                        notificationPermissionIsGranted = false
                        Log.d("LOG_TAG", "notification permission denied by the user")
                    }
                }
            // If location permission is not granted yet
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Prompt user for location permission if not granted yet
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // Else, location has been granted
            } else {
                notificationPermissionIsGranted = true
                Log.d("LOG_TAG", "notification permission granted by the user yet")
            }
        } else {
            notificationPermissionIsGranted = true
        }
    }
}