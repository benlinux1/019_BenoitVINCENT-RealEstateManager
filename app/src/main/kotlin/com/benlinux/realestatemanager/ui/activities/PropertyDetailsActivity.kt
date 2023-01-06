package com.benlinux.realestatemanager.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.adapters.PictureAdapter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.getLatLngFromPropertyFormattedAddress
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PropertyDetailsActivity: AppCompatActivity() {

    // Views
    private var propertyId: String? = null
    private lateinit var title: TextView
    private lateinit var area: TextView
    private lateinit var surface: TextView
    private lateinit var titleRooms: TextView
    private lateinit var titleBathrooms: TextView
    private lateinit var titleBedrooms: TextView
    private lateinit var numberOfRooms: TextView
    private lateinit var numberOfBathrooms: TextView
    private lateinit var numberOfBedrooms: TextView
    private lateinit var price: TextView
    private lateinit var description: TextView
    private lateinit var streetNumberAndStreetName: TextView
    private lateinit var complement: TextView
    private lateinit var postalCodeAndCity: TextView
    private lateinit var country: TextView
    private lateinit var updateButton: FloatingActionButton
    private lateinit var soldBackground: LinearLayout
    private lateinit var propertyRealtor : TextView
    private lateinit var dateOfPublication: TextView
    private lateinit var dateOfSold: TextView
    private lateinit var dateOfPublicationTitle: TextView
    private lateinit var dateOfSoldTitle: TextView

    // The recycler view and the list + adapter for pictures gallery
    private lateinit var picturesRecyclerView: RecyclerView
    private lateinit var picturesList: MutableList<Picture?>
    private lateinit var pictureAdapter: PictureAdapter

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // The displayed property
    private var property: Property? = null

    // The mapview and map that shows property location
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapView: MapView

    // For preferences data
    private var userIsRealtor = false
    private var propertyIsInFavorites = false
    private var creatorId: String? = ""

    // Slider Image
    private lateinit var imageSlider: ImageSlider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)

        setToolbar()
        setViews()
        setMap()
        setViewModel()
        retrievePropertyId()
        retrieveCreatorId()
        checkIfUserIsRealtor()
        setFloatingButton()
        setListenerOnFloatingButton()
    }

    // Initialize map view and google map
    private fun setMap() {
        mapView = findViewById(R.id.map)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{ map ->
                MapsInitializer.initialize(applicationContext)
                mGoogleMap = map
                map.uiSettings.isMapToolbarEnabled = false

                // When map is ready, set all property's data
                retrieveAndSetPropertyData()
            }
        }
    }

    // Retrieve property id
    private fun retrievePropertyId() {
        propertyId = intent.extras?.getString("PROPERTY_ID")
        Log.d("ID", propertyId.toString())
    }

    // Retrieve property id
    private fun retrieveCreatorId() {
        creatorId = intent.extras?.getString("PROPERTY_CREATOR_ID")
        creatorId?.let { Log.d("CREATOR_ID", it) }
    }

    // Toolbar configuration
    private fun setToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = resources.getString(R.string.details_activity_title)
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

    // Set all text views
    private fun setViews() {
        title = findViewById(R.id.property_details_information_title)
        area = findViewById(R.id.property_details_information_area)
        surface = findViewById(R.id.property_details_surface_text)
        price = findViewById(R.id.property_details_price_text)
        numberOfRooms = findViewById(R.id.property_details_rooms_text)
        numberOfBedrooms = findViewById(R.id.property_details_bedrooms_text)
        numberOfBathrooms = findViewById(R.id.property_details_bathrooms_text)
        description = findViewById(R.id.property_details_description_text)
        titleRooms = findViewById(R.id.property_details_rooms_title)
        titleBedrooms = findViewById(R.id.property_details_bedrooms_title)
        titleBathrooms = findViewById(R.id.property_details_bathrooms_title)
        streetNumberAndStreetName = findViewById(R.id.property_details_location_street)
        complement = findViewById(R.id.property_details_location_complement)
        postalCodeAndCity = findViewById(R.id.property_details_location_postal_code_and_city)
        country = findViewById(R.id.property_details_location_country)
        updateButton = findViewById(R.id.property_details_update_button)
        propertyRealtor = findViewById(R.id.property_details_realtor_text)
        dateOfPublication = findViewById(R.id.property_details_publication_date_text)
        dateOfSold = findViewById(R.id.property_details_sold_date_text)
        dateOfPublicationTitle = findViewById(R.id.property_details_publication_date_title)
        dateOfSoldTitle = findViewById(R.id.property_details_sold_date_title)
    }

    // Configuring ViewModel from ViewModelFactory
    private fun setViewModel() {
        // Define ViewModel
        val viewModelFactory = ViewModelFactory(this.application)
        propertyViewModel = ViewModelProvider(this, viewModelFactory)[PropertyViewModel::class.java]
    }

    // Retrieve and set all property data
    private fun retrieveAndSetPropertyData() {
        propertyViewModel.getPropertyById(propertyId!!.toInt() )
        // Set observer on current property
        propertyViewModel.currentProperty?.observe(this) { actualProperty ->
            property = actualProperty
            setPropertyData()
            retrievePropertyPictures()
            configurePhotoGallery()

            val latLng: LatLng = getLatLngFromPropertyFormattedAddress(property!!.address, this)
            setMarkersForProperty(mGoogleMap, latLng)
            setSoldView(property!!.isAvailable)
        }
    }

    // Indicate if property is sold according to its availability status
    private fun setSoldView(availableProperty: Boolean) {
        soldBackground = findViewById(R.id.background_sold)
        if (availableProperty) {
            soldBackground.visibility = View.GONE
        } else {
            soldBackground.visibility = View.VISIBLE
        }
    }

    // Retrieve property's pictures and add each of them in pictures list
    private fun retrievePropertyPictures() {
        picturesList = mutableListOf()
        if (this.property != null) {
            for (picture: Picture? in property!!.pictures) {
                picturesList.add(picture)
            }
        }
    }

    // Configure Pictures Gallery
    private fun configurePhotoGallery() {
        // Define layout & adapter
        picturesRecyclerView = findViewById(R.id.details_pictures_list)
        pictureAdapter = PictureAdapter(picturesList, this, userIsRealtor )
        picturesRecyclerView.adapter = pictureAdapter
        setEasyScrollFeature()
    }

    // Show arrows on list sides to indicate scroll possibility
    private fun setEasyScrollFeature() {
        val arrowRight: ImageView = findViewById(R.id.list_arrow_right)
        val arrowLeft: ImageView = findViewById(R.id.list_arrow_left)

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

    // Set property data in all text views
    private fun setPropertyData() {
        if (this.property != null) {

            title.text = property!!.name // title
            area.text = property!!.area // area

            setSurface(property!!)
            setPrice(property!!)
            setRooms(property!!)
            setBedrooms(property!!)
            setBathrooms(property!!)

            description.text = property!!.description // Description

            setPropertyPictures(property!!)
            setFullAddress(property!!)

            setPropertyRealtor(property!!) // property realtor
            setPropertyDates(property!!) // publication / sold date

            Log.d("ACTUAL PROPERTY", property.toString())
        }
    }

    private fun setPropertyDates(property: Property) {
        if (property.isAvailable) {
            dateOfPublication.text = property.creationDate
            dateOfSold.visibility = View.GONE
            dateOfSoldTitle.visibility = View.GONE
        } else {
            dateOfSold.text = property.soldDate
            dateOfPublication.visibility = View.GONE
            dateOfPublicationTitle.visibility = View.GONE
        }
    }

    private fun setPropertyRealtor(property: Property) {
        propertyRealtor.text = buildString {
            append(property.realtor.firstName)
            append(" ")
            append(property.realtor.lastName)
        }
    }


    private fun setSurface(property: Property) {
        // Formatted surface
        surface.text = buildString {
            append(property.surface)
            append(" m²")
        }
    }

    private fun setPrice(property: Property) {
        // Formatted price
        val formattedPrice = String.format("%,d", property.price)
        price.text = buildString {
            append("$ ")
            append(formattedPrice)
        }
    }

    // Display or Hide number of rooms / bathrooms / bedrooms according to data
    private fun setRooms(property: Property) {
        if (property.numberOfRooms > 0) {
            numberOfRooms.text = property.numberOfRooms.toString()
            titleRooms.visibility = View.VISIBLE
        } else {
            numberOfRooms.visibility = View.GONE
            titleRooms.visibility = View.GONE
        }
    }

    // Display or Hide number of bedrooms according to data
    private fun setBedrooms(property: Property) {
        if (property.numberOfBedrooms > 0) {
            numberOfBedrooms.text = property.numberOfBedrooms.toString()
            titleBedrooms.visibility = View.VISIBLE
        } else {
            numberOfBedrooms.visibility = View.GONE
            titleBedrooms.visibility = View.GONE
        }
    }

    // Display or Hide number of bathrooms according to data
    private fun setBathrooms(property: Property) {
        if (property.numberOfBathrooms > 0) {
            numberOfBathrooms.text = property.numberOfBathrooms.toString()
            titleBathrooms.visibility = View.VISIBLE
        } else {
            numberOfBathrooms.visibility = View.GONE
            titleBathrooms.visibility = View.GONE
        }
    }

    // Image Slider for property pictures
    private fun setPropertyPictures(property: Property) {

        imageSlider = findViewById(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()
        if (property.pictures.isNotEmpty()) {
            for (picture in property.pictures) {
                imageList.add(SlideModel(picture?.url, picture?.room, ScaleTypes.CENTER_CROP))
            }
        } else {
            imageList.add(SlideModel(R.mipmap.no_photo_bis, ScaleTypes.CENTER_CROP))
        }
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }


    // Display Property full address data
    private fun setFullAddress(property: Property) {
        // Street number & street name
        streetNumberAndStreetName.text = buildString {
            append(property.address.streetNumber)
            append(" ")
            append(property.address.streetName)
        }
        // Complement
        if (property.address.complement!!.isNotEmpty()) {
            complement.text = property.address.complement
            complement.visibility = View.VISIBLE
        } else { complement.visibility = View.GONE }

        // Postal code & city
        postalCodeAndCity.text = buildString {
            append(property.address.postalCode)
            append(" ")
            append(property.address.city!!.uppercase())
        }

        // Country
        country.text = property.address.country!!.uppercase()
    }


    // Set custom marker for displayed property on map, according to its latlng
    private fun setMarkersForProperty(googleMap: GoogleMap, latLng: LatLng?) {

        mGoogleMap = googleMap

        // Define custom marker icon
        val propertyMarker = R.drawable.marker_property

        // Then, define marker options (property's position & icon)
        val markerOptions = MarkerOptions()
            .position(latLng!!)
            .icon(BitmapDescriptorFactory.fromResource(propertyMarker))

        // Set marker
        googleMap.addMarker(markerOptions)

        // Center & zoom camera on property location
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 15f
            )
        )
    }

    private fun checkIfUserIsRealtor() {
        val sharedPreferences = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        userIsRealtor = sharedPreferences.getBoolean("realtor", true)
        Log.d("REALTOR STATUS", userIsRealtor.toString())
    }

    // Change floating button according to user status (5 possibilities)
    private fun setFloatingButton() {
        if (userIsRealtor) {
            if ( creatorId == UserManager.getCurrentUser()?.uid ) {
                updateButton.setImageResource(R.drawable.ic_details_edit_24) // Edition button if realtor & creator
            } else {
                updateButton.visibility = View.GONE // invisible button if realtor is not creator
            }
        } else {
            if (UserManager.isCurrentUserLogged()) {
                updateButton.setImageResource(R.drawable.ic_details_like_empty) // Empty favorite if user
                UserManager.getUserData()?.addOnSuccessListener { user ->
                    if (user != null) {
                        for (favorite in user.favorites) {
                            if (favorite == propertyId) { // favorite full if property is in user's favorites
                                updateButton.setImageResource(R.drawable.ic_details_like_full)
                                propertyIsInFavorites = true
                            }
                        }
                    }
                }
            } else {
                updateButton.visibility = View.GONE // invisible button if user not logged
            }
        }
    }

    // Set floating button actions on click
    private fun setListenerOnFloatingButton() {
        updateButton.setOnClickListener {
            // If realtor, go to update property
            if (userIsRealtor) {
                val updatePropertyIntent = Intent(this, UpdatePropertyActivity::class.java)
                updatePropertyIntent.putExtra("PROPERTY_ID", propertyId)
                startActivity(updatePropertyIntent)
                finish()
            // if simple user, add property to favorites
            } else {
                if (!propertyIsInFavorites) {
                    UserManager.addPropertyToFavorites(propertyId)
                    propertyIsInFavorites = true
                    updateButton.setImageResource(R.drawable.ic_details_like_full)
                } else {
                    UserManager.removePropertyFromFavorites(propertyId)
                    propertyIsInFavorites = false
                    updateButton.setImageResource(R.drawable.ic_details_like_empty)
                }
            }
        }
    }


    // Lifecycles
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}