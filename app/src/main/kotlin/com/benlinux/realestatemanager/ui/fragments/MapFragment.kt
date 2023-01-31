package com.benlinux.realestatemanager.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.injections.ViewModelFactory
import com.benlinux.realestatemanager.ui.activities.MainActivity
import com.benlinux.realestatemanager.ui.activities.PropertyDetailsActivity
import com.benlinux.realestatemanager.ui.adapters.InfoWindowForMapAdapter
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.getLatLngFromPropertyFormattedAddress
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var locationPermissionIsGranted: Boolean = false
    private lateinit var client: FusedLocationProviderClient

    // The viewModel that contains data
    private lateinit var propertyViewModel: PropertyViewModel

    // The user actual location
    private var userLocation: LatLng? = null

    // List of all current properties in the application
    private var mProperties: MutableList<Property?> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Init location client
        client = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Configure view model
        configureViewModel()

        // Hide Add Property Button
        (activity as MainActivity).hideAddButton()

        // Check if user has been located
        userLocation = getUserLocationFromMain()

        return inflater.inflate(R.layout.fragment_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check location permissions
        checkLocationPermissions()

        // Define map fragment
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!

        // Set async google map
        mapFragment.getMapAsync(this)
    }

    // Configuring ViewModel from ViewModelFactory
    private fun configureViewModel() {

        // Define ViewModel
        propertyViewModel = ViewModelProvider(this,
            ViewModelFactory.getInstance(requireContext())!!
        )[PropertyViewModel::class.java]

    }

    // Retrieve properties and set markers on google map for each
    private fun setProperties() {
        // Set observer on properties list
        propertyViewModel.currentProperties?.observe(viewLifecycleOwner) { listOfProperties ->
            mProperties = listOfProperties
            // Set markers for each property on google map
            setMarkersForProperties(mGoogleMap, mProperties)
        }
    }

    // Google map callback
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        // When map is loaded
        mGoogleMap = googleMap
        googleMap.mapType = MAP_TYPE_NORMAL

        if (locationPermissionIsGranted) {
            // Set marker & move camera if user has been located yet
            if (this.getUserLocation() != null) {
                setMarkerForUserLocation(googleMap, userLocation!!, "actual" )
            // else get user location
            } else {
                getCurrentLocation()
            }
        }

        // update UI with or without blue point location and map centering button
        updateLocationUI()

        // Set map listeners
        setListenerOnMapClick(googleMap)
        setListenerOnMyLocationIcon(googleMap)
        setListenerOnMyLocationButton(googleMap)

        // Set custom info window layout
        googleMap.setInfoWindowAdapter(InfoWindowForMapAdapter(requireContext()))

        // Set click listener on Info window
        setInfoWindowClickListener(googleMap)

        // Set properties with markers
        setProperties()
    }

    private fun getUserLocation(): LatLng? {
        return this.userLocation
    }


    // Check for location permission
    private fun checkLocationPermissions() {
        // Callback for location permission
        val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

                if (isGranted) { // If permission is granted
                    locationPermissionIsGranted = true
                    Log.d("LOG_TAG", "permission granted by the user")

                } else { // If permission is not granted
                    Log.d("LOG_TAG", "permission denied by the user")
                }
            }

        // If location permission is not granted yet
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Prompt user for location permission if not granted yet
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        // Else, location has been granted
        } else {
            locationPermissionIsGranted = true
            Log.d("LOG_TAG", "permission granted by the user yet")
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        // Initialize location manager
        val locationManager: LocationManager = requireActivity()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        ) {
            try {
                // When Location is enabled, get last location
                client.lastLocation
                    .addOnCompleteListener { task ->
                        // Initialize location
                        val location = task.result

                        // Check condition
                        if (location != null) {

                            // Save actual location to userLocation variable
                            userLocation = LatLng(location.latitude, location.longitude)
                            setUserLocation(LatLng(location.latitude, location.longitude))

                            // Set marker for user position
                            setMarkerForUserLocation(
                                mGoogleMap,
                                LatLng(location.latitude, location.longitude),
                                "actual"
                            )
                            // Move Camera
                            moveCamera(mGoogleMap, userLocation!!)
                            animateCamera(mGoogleMap, userLocation!!, 10f)

                        } else {

                            // When location result is null, initialize location request
                            val locationRequest: LocationRequest =
                                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                                    .setWaitForAccurateLocation(false)
                                    .setMinUpdateIntervalMillis(5)
                                    .setMaxUpdateDelayMillis(1)
                                    .build()

                            // Initialize Location callback
                            val locationCallback: LocationCallback = object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    // Initialize last location
                                    val lastLocation: Location? = locationResult.lastLocation
                                    // save last location in actualLocation variable
                                    if (lastLocation != null) {
                                        userLocation =
                                            LatLng(lastLocation.latitude, lastLocation.longitude)
                                        setUserLocation(
                                            LatLng(
                                                lastLocation.latitude,
                                                lastLocation.longitude
                                            )
                                        )
                                    }

                                    // Set marker for user position
                                    if (lastLocation != null) {
                                        setMarkerForUserLocation(
                                            mGoogleMap,
                                            LatLng(lastLocation.latitude, lastLocation.longitude),
                                            "last"
                                        )
                                    }

                                    // Move Camera on user location
                                    moveCamera(mGoogleMap, userLocation!!)
                                    animateCamera(mGoogleMap, userLocation!!, 10f)
                                }
                            }
                            // Request location updates
                            client.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                        }
                    }
            } catch (ex: Exception) {
                Log.e("Exception: %s", ex.message!!)
            }
        } else {
            // Open location settings
            startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    // Set realtor location in main activity variable
    private fun setUserLocation(latLng: LatLng) {
        (activity as MainActivity).userLocation = latLng
    }

    // Retrieve realtor location from main activity
    private fun getUserLocationFromMain(): LatLng? {
        return (activity as MainActivity).userLocation
    }

    // Add marker for user location
    private fun setMarkerForUserLocation(googleMap: GoogleMap, userLocation: LatLng, locationType: String) {
        mGoogleMap = googleMap
        when (locationType) {
            "actual" -> {
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Actual location"))
            }
            "last" -> {
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Last known location"))
            }
            "search" -> {
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Search location"))
            }
        }
    }

    // Move google map camera to specific location
    private fun moveCamera(googleMap: GoogleMap, location: LatLng) {
        mGoogleMap = googleMap
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 4F
            )
        )
    }

    // Animate google map camera to specific location
    private fun animateCamera(googleMap: GoogleMap, location: LatLng, zoom: Float) {
        mGoogleMap = googleMap
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), zoom
            )
        )
    }

    private fun setListenerOnMapClick(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        // Set listener for clicks on Map
        googleMap.setOnMapClickListener { latLng ->
            // When clicked on map
            // Remove all marker
            googleMap.clear()
            // save new location
            setUserPosition(LatLng(latLng.latitude, latLng.longitude))
            // Animating to zoom the marker
            animateCamera(googleMap, latLng, 15f)
            // Add marker on map
            setMarkerForUserLocation(mGoogleMap, latLng, "search")
            // Set markers for properties
            setMarkersForProperties(mGoogleMap, mProperties)
        }
    }

    private fun setUserPosition(latLng: LatLng) {
        userLocation = LatLng(latLng.latitude, latLng.longitude)
    }

    private fun setListenerOnMyLocationIcon(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.setOnMyLocationClickListener { location ->
            googleMap.clear()
            val userPosition =
                LatLng(location.latitude, location.longitude)
            // Actualize user fictive position on click
            setUserPosition(userPosition)
            // Set marker for user
            setMarkerForUserLocation(mGoogleMap, userPosition, "actual")
            // Set markers for properties
            setMarkersForProperties(mGoogleMap, mProperties)
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), 15F
                )
            )
        }
    }


    private fun setListenerOnMyLocationButton(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.setOnMyLocationButtonClickListener {
            googleMap.clear()
            getCurrentLocation()
            setMarkersForProperties(mGoogleMap, mProperties)
            true
        }
    }

    // Set listener on restaurant's info window to launch restaurant's details page
    @SuppressLint("PotentialBehaviorOverride")
    private fun setInfoWindowClickListener(mGoogleMap: GoogleMap) {
        mGoogleMap.setOnInfoWindowClickListener { marker ->
            val propertyDetailsIntent = Intent(context, PropertyDetailsActivity::class.java)

            // Retrieve property id in tag
            val propertyId = marker.tag.toString().split("/")[0]
            // Retrieve property realtor id in extra
            val propertyRealtorId = marker.tag.toString().split("/")[1]

            // Send property id in order to get it in details activity
            propertyDetailsIntent.putExtra("PROPERTY_ID", propertyId)
            propertyDetailsIntent.putExtra("PROPERTY_CREATOR_ID", propertyRealtorId)
            if (propertyId != "null") {
                startActivity(propertyDetailsIntent)
            }
        }
    }

    /**
     * Add or remove user location with blue point marker on Google Map
     * and map centering button according to permissions
     */
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionIsGranted) {
                // When location is granted, mark current location with blue point
                mGoogleMap.isMyLocationEnabled = true
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = true
                mGoogleMap.uiSettings.isZoomControlsEnabled = true
            } else {
                // When location is not granted, don't mark map with blue point
                mGoogleMap.isMyLocationEnabled = false
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }


    // Set custom marker for each property in properties list
    private fun setMarkersForProperties(googleMap: GoogleMap, properties: MutableList<Property?>) {

        mGoogleMap = googleMap

        // Define custom marker icon
        val propertyMarker = R.drawable.marker_property

        // Customize marker title, position, snippet & icon for each property
        for (property: Property? in properties) {
            val title = property?.name
            val address = property!!.address
            val latLng: LatLng = getLatLngFromPropertyFormattedAddress(address, requireContext())
            val price = property.price.toString()

            val propertyId = property.id.toString()
            val propertyRealtorId = property.realtor.id

            // Then, define marker options (property's title, address, position, icon)
            val markerOptions = MarkerOptions()
                .title(title)
                .position(latLng)
                .snippet(price)
                .icon(BitmapDescriptorFactory.fromResource(propertyMarker))

            // Set property id & realtor id in tag (used to retrieve data in details activity)
            googleMap.addMarker(markerOptions)?.tag = "$propertyId/$propertyRealtorId"

        }
    }

    override fun onResume() {
        super.onResume()
        // Call map when ready
        mapFragment.getMapAsync(this)
    }
}