package com.benlinux.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.getLatLngFromPropertyFormattedAddress
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PropertyDetailsActivity: AppCompatActivity(), OnMapReadyCallback {


    // Views
    private lateinit var mainPicture: ImageView
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

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // The displayed property
    private var property: Property? = null

    private lateinit var mGoogleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)

        setToolbar()
        setViews()
        setMap()
        retrievePropertyId()
        configureViewModel()
        retrievePropertyData()

    }

    // Set google map
    private fun setMap() {
        // Define map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    // Google map callback
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        // When map is loaded
        mGoogleMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

    }

    // Retrieve property id
    private fun retrievePropertyId() {
        propertyId = intent.extras?.getString("PROPERTY_ID")
        Log.d("ID", propertyId.toString())
    }

    // Toolbar configuration
    private fun setToolbar() {
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

    private fun setViews() {
        mainPicture = findViewById(R.id.property_details_main_picture)
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
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {
        // Define ViewModel
        val viewModelFactory = ViewModelFactory(this.application)
        propertyViewModel = ViewModelProvider(this, viewModelFactory)[PropertyViewModel::class.java]
    }

    private fun retrievePropertyData() {
        propertyViewModel.getPropertyById(propertyId!!.toInt() )
        // Set observer on current property
        propertyViewModel.currentProperty?.observe(this) { actualProperty ->
            property = actualProperty
            setPropertyData()
            setMarkersForProperty(mGoogleMap)
        }
    }

    private fun setPropertyData() {
        if (this.property != null) {
            title.text = property!!.name
            area.text = property!!.area

            // Formatted surface
            surface.text = buildString {
                append(property!!.surface)
                append(" m²")
            }

            // Formatted price
            val formattedPrice = String.format("%,d", property!!.price)
            price.text = buildString {
                append("$ ")
                append(formattedPrice)
            }

            // Display or Hide number of rooms / bathrooms / bedrooms according to data
            if (property!!.numberOfRooms > 0) {
                numberOfRooms.text = property!!.numberOfRooms.toString()
                titleRooms.visibility = View.VISIBLE
            } else {
                numberOfRooms.visibility = View.GONE
                titleRooms.visibility = View.GONE
            }

            if (property!!.numberOfBedrooms > 0) {
                numberOfBedrooms.text = property!!.numberOfBedrooms.toString()
                titleBedrooms.visibility = View.VISIBLE
            } else {
                numberOfBedrooms.visibility = View.GONE
                titleBedrooms.visibility = View.GONE
            }

            if (property!!.numberOfBathrooms > 0) {
                numberOfBathrooms.text = property!!.numberOfBathrooms.toString()
                titleBathrooms.visibility = View.VISIBLE
            } else {
                numberOfBathrooms.visibility = View.GONE
                titleBathrooms.visibility = View.GONE
            }

            description.text = property!!.description

            // Main picture
            if (property!!.pictures.isNotEmpty())
                Glide.with(this)
                    .load(property!!.pictures[0]?.url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mainPicture)

            // Address
            // Street number & street name
            streetNumberAndStreetName.text = buildString {
                append(property!!.address.streetNumber)
                append(" ")
                append(property!!.address.streetName)
            }
            // Complement
            if (property!!.address.complement.isNotEmpty()) {
                    complement.text = property!!.address.complement
                    complement.visibility = View.VISIBLE
            } else { complement.visibility = View.GONE }

            // Postal code & city
            postalCodeAndCity.text = buildString {
                append(property!!.address.postalCode)
                append(" ")
                append(property!!.address.city)
            }

            // Country
            country.text = property!!.address.country.uppercase()
        }
    }

    // Set custom marker for displayed property on map
    private fun setMarkersForProperty(googleMap: GoogleMap) {

        mGoogleMap = googleMap

        // Define custom marker icon
        val propertyMarker = R.drawable.marker_property

        // Customize marker position & icon
        if (property != null) {
            val latLng: LatLng? = getLatLngFromPropertyFormattedAddress(property!!.address, this)

            // Then, define marker options (property's position & icon)
            val markerOptions = MarkerOptions()
                .position(latLng!!)
                .icon(BitmapDescriptorFactory.fromResource(propertyMarker))

            // Set marker
            googleMap.addMarker(markerOptions)

            // Center & zoom camera on property location
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, 15F
                )
            )
        }
    }
}