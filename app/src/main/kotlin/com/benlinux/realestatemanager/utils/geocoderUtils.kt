package com.benlinux.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*


/**
 * Retrieve Latitude & Longitude with Geocoder
 * according to Android Build
 * @param address the address of the property
 * @param context the context of the application
 * @return LatLng the latitude & the longitude of the property in LatLng type
 * @author BenLinux1
 */
fun getLatLngFromAddress(address: String, context: Context): LatLng? {

    var location: LatLng? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val coder = Geocoder(context)
        coder.getFromLocationName(
            address,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    addresses.forEach {
                        //do your work android 13
                        Log.d("Latitude", "onGeocode: ${it.latitude}")
                        Log.d("Longitude", "onGeocode: ${it.longitude}")
                        location = LatLng(it.latitude, it.longitude)
                    }

                }

                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    Log.d("GEO ERROR TIRAMISU", "onError: $errorMessage")
                    Toast.makeText(
                        context,
                        "City not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    } else {
        // less than Android 13, call function below
        location = getLocationFromAddress(address, context)
    }
    return location
}

// Retrieve Latitude & Longitude from Address (Android < 13)
@Suppress("DEPRECATION")
private fun getLocationFromAddress(strAddress: String, context: Context): LatLng? {
    val coder = Geocoder(context)
    var latLng: LatLng? = null
    try {
        val address: MutableList<Address>? = coder.getFromLocationName(strAddress, 1)

        address?.let {
            if (it.size > 0) {

                address[0].let { add ->
                    latLng = LatLng(
                        (add.latitude),
                        (add.longitude)
                    )
                }
            }
            Log.d("GEOCODER", "getLocationFromAddress: $latLng")
        }

    } catch (e: IOException) {
        e.printStackTrace()
        Log.d("GEOCODER", "getLocationFromAddress: ${e.message}")
    }
    return latLng
}


/**
 * Retrieve Property Address with Geocoder
 * according to Android Build
 * @param location the LatLng of the property
 * @param context the context of the application
 * @return PropertyAddress the address of the property in PropertyAddress type
 * @author BenLinux1
 */
fun getAddressFromLocation(location: LatLng, context: Context): PropertyAddress? {

    var address: PropertyAddress? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val coder = Geocoder(context)
        coder.getFromLocation(
                location.latitude,
                location.longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        addresses.forEach {
                            // Build address model in android 13+
                            Log.d("Street Number", "onGeocode: ${it.subThoroughfare}")
                            Log.d("Street Name", "onGeocode: ${it.thoroughfare}")
                            Log.d("Postal Code", "onGeocode: ${it.postalCode}")
                            Log.d("City", "onGeocode: ${it.locality}")
                            Log.d("Country", "onGeocode: ${it.countryName}")

                            address = PropertyAddress(it.subThoroughfare, it.thoroughfare, "",
                                it.postalCode, it.locality, it.countryName)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Log.d("GEO ERROR TIRAMISU", "onError: $errorMessage")
                        Toast.makeText(
                            context,
                            "Address not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

    } else {
        // less than Android 13, call function below
        address = getFormattedAddressFromLatLng(location, context)
    }
    return address
}


// Retrieve Property address from LatLng (Android < 13)
@Suppress("DEPRECATION")
private fun getFormattedAddressFromLatLng(latLng: LatLng, context: Context): PropertyAddress? {
    val geocoder = Geocoder(context)
    val addresses: List<Address>?
    var propertyAddress: PropertyAddress? = null

    try {

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        val addressResult = addresses!![0]

        // Build address model in android less than 13
        Log.d("Street Number", "onGeocode: ${addressResult.subThoroughfare}")
        Log.d("Street Name", "onGeocode: ${addressResult.thoroughfare}")
        Log.d("Postal Code", "onGeocode: ${addressResult.postalCode}")
        Log.d("City", "onGeocode: ${addressResult.locality}")
        Log.d("Country", "onGeocode: ${addressResult.countryName}")
        val mStreetNumber = addressResult.subThoroughfare
        val mStreetName = addressResult.thoroughfare
        val mPostalCode = addressResult.postalCode
        val mCity = addressResult.locality
        val mCountry = addressResult.countryName

        propertyAddress = PropertyAddress(mStreetNumber, mStreetName, "", mPostalCode, mCity, mCountry)

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return propertyAddress
}
