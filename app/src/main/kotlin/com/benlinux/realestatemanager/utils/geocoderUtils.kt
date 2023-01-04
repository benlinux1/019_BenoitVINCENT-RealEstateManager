package com.benlinux.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
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


fun getLatLngFromPropertyFormattedAddress(address: PropertyAddress, context: Context): LatLng {

    // Convert property address to string
    val addressString: String = buildString {
        append(address.streetNumber)
        append(" ")
        append(address.streetName)
        append(" ")
        append(address.postalCode)
        append(" ")
        append(address.city)
    }

    // call geocoder to convert string & return lat lng
    return getLocationFromAddress(addressString, context)

}

// Retrieve Latitude & Longitude from Address under String format
@Suppress("DEPRECATION")
private fun getLocationFromAddress(strAddress: String, context: Context): LatLng {
    val coder = Geocoder(context)
    var latLng = LatLng(0.0,0.0)
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

    return getFormattedAddressFromLatLng(location, context)
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
        var mCity = addressResult.locality
        Log.d("Locality", "onGeocode: ${addressResult.locality}")
        if (addressResult.locality == null) {
            mCity = addressResult.subLocality
            Log.d("SubLocality", "onGeocode: ${addressResult.subLocality}")
            if (addressResult.subLocality == null) {
                mCity = addressResult.subAdminArea
                Log.d("SubAdminArea", "onGeocode: ${addressResult.subAdminArea}")
            }
        }
        val mCountry = addressResult.countryName

        propertyAddress =
            PropertyAddress(mStreetNumber, mStreetName, "", mPostalCode, mCity, mCountry)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return propertyAddress
}
