package com.benlinux.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.utils.getAddressFromLocation
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.util.*

class InfoWindowForMapAdapter(var context: Context) : InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        @SuppressLint("InflateParams")
        val view: View = inflater.inflate(R.layout.custom_info_window_for_map, null)

        // Get reference to the TextView to set the id of the property
        val propertyId = view.findViewById<TextView>(R.id.property_id)

        // Getting the property's position from marker position
        val latLng: LatLng = marker.position

        // Getting the property's price from marker snippet
        val price: String? = marker.snippet

        // Getting the property's formatted address according to marker position
        val formattedAddress: PropertyAddress? = getAddressFromLocation(latLng, context)

        // Get reference to the TextView to set property's title
        val propertyTitle = view.findViewById<TextView>(R.id.title)

        // Get reference to the TextView to set street & street number
        val propertyStreetNumberAndName = view.findViewById<TextView>(R.id.street)

        // Get reference to the TextView to set postal code & city
        val propertyPostalCodeAndCity = view.findViewById<TextView>(R.id.postalCodeAndCity)

        // Get reference to the TextView to set price
        val propertyPrice = view.findViewById<TextView>(R.id.price)

        // Get reference to the info window button
        val seeDetailsButton = view.findViewById<Button>(R.id.seeDetailsButton)
        
        // Disable details button if place doesn't get id (used for user location)
        if (marker.tag == null) {
            seeDetailsButton.visibility = View.GONE
            propertyPrice.visibility = View.GONE
        } else {
            propertyId.text = marker.tag.toString()
        }

        // Set title to uppercase
        propertyTitle.text = marker.title?.uppercase()

        // Set property's formatted street number and street name
        propertyStreetNumberAndName.text = buildString {
            append(formattedAddress?.streetNumber)
            append(" ")
            append(formattedAddress?.streetName)
        }

        // Set property's formatted postal code and city
        propertyPostalCodeAndCity.text = buildString {
            append(formattedAddress?.postalCode)
            append(" ")
            append(formattedAddress?.city)
        }

        // Hide address fields if formatted address return null
        if (formattedAddress == null) {
            propertyStreetNumberAndName.visibility = View.GONE
            propertyPostalCodeAndCity.visibility = View.GONE
        }

        // Formatted price
        val formattedPrice = String.format("%,d", price?.toInt() )
        propertyPrice.text = buildString {
            append(formattedPrice)
            append(" $")
        }

        // Returning the view containing InfoWindow contents
        return view
    }

}